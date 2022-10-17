package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.payneteasy.tlv.HexUtil;
import com.vecentek.back.constant.CalibrationDataConstant;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.mapper.DkmPhoneCalibrationDataMapper;
import com.vecentek.back.util.RedisUtils;
import com.vecentek.back.util.UploadUtil;
import com.vecentek.common.response.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * DkmPhoneCalibrationData 表服务实现类
 *
 * @author tangc
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@Service
public class DkmPhoneCalibrationDataServiceImpl {
    @Resource
    private DkmPhoneCalibrationDataMapper dkmPhoneCalibrationDataMapper;
    @Resource
    private RedisUtils redisUtils;


    /**
     * 查询分页数据
     *
     * @return 对象列表
     */
    public PageResp selectForPage(int pageIndex, int pageSize, String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand) {
        // 获取当前表中的总记录
        Page<DkmPhoneCalibrationData> page = new Page<>(pageIndex, pageSize);

        LambdaQueryWrapper<DkmPhoneCalibrationData> queryWrapper = Wrappers.<DkmPhoneCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmPhoneCalibrationData::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmPhoneCalibrationData::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleType), DkmPhoneCalibrationData::getVehicleType, vehicleType)
                .like(StrUtil.isNotBlank(phoneBrand), DkmPhoneCalibrationData::getPhoneBrand, phoneBrand);
        page = dkmPhoneCalibrationDataMapper.selectPage(page, queryWrapper);

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 通过主键更新单条手机标定数据
     *
     * @param dkmPhoneCalibrationData 手机标定数据
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateDkmPhoneCalibrationDataById(DkmPhoneCalibrationData dkmPhoneCalibrationData) {

        dkmPhoneCalibrationData.setUpdateTime(new Date());
        if (dkmPhoneCalibrationData.getPersonalAndCalibrationString().length() != ExcelConstant.CALIBRATION_LENGTH) {
            return PageResp.fail("标定数据必须是32字节");
        }
        try {
            byte[] bytes = HexUtil.parseHex(dkmPhoneCalibrationData.getPersonalAndCalibrationString());
        } catch (Exception e) {
            return PageResp.fail("标定数据解析错误！请检查数据是否正常");
        }
        dkmPhoneCalibrationDataMapper.updateById(dkmPhoneCalibrationData);

        return PageResp.success("更新成功");
    }


    /**
     * 批量导入手机标定数据
     *
     * @param file 文件
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp importByExcel(MultipartFile file) {
        try {
            PageResp pageResp = UploadUtil.checkFile(file);
            if (pageResp != null) {
                return pageResp;
            }
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            reader.addHeaderAlias("车辆型号", "vehicleModel");
            reader.addHeaderAlias("手机品牌", "phoneBrand");
            reader.addHeaderAlias("手机型号", "phoneModel");
            reader.addHeaderAlias("车辆品牌", "vehicleBrand");
            reader.addHeaderAlias("车型", "vehicleType");
            reader.addHeaderAlias("标定数据", "personalAndCalibrationString");
            List<DkmPhoneCalibrationData> calibrationList = reader.readAll(DkmPhoneCalibrationData.class);
            reader.close();
            //车辆型号 + 手机型号 形成的联合主键进行冒泡比对来查重
            for (int i = 0; i < calibrationList.size(); i++) {
                String beforeID = calibrationList.get(i).getVehicleModel() + calibrationList.get(i).getPhoneModel();
                for (int j = i + 1; j < calibrationList.size(); j++) {
                    String afterID = calibrationList.get(j).getVehicleModel() + calibrationList.get(j).getPhoneModel();
                    if(StrUtil.equals(beforeID,afterID)){
                        return PageResp.fail("导入失败，excel文件车辆型号和手机型号有重复!");
                    }
                }
            }
            if (calibrationList.size() == 0) {
                return PageResp.fail("导入失败，请检查excel文件！");
            }
            int rowIndex = 1;
            for (DkmPhoneCalibrationData calibration : calibrationList) {
                rowIndex++;
                String personalAndCalibrationString = calibration.getPersonalAndCalibrationString().replace(" ", "");
                calibration.setPersonalAndCalibrationString(personalAndCalibrationString);
                calibration.setCreateTime(new Date());
                if (StringUtils.isBlank(calibration.getVehicleModel())) {
                    return PageResp.fail("第 " + rowIndex + " 行导入的车型数据不能为空！");
                }
                if (calibration.getPersonalAndCalibrationString().length() != ExcelConstant.CALIBRATION_LENGTH) {
                    return PageResp.fail("第 " + rowIndex + " 行导入的标定数据必须是32字节！");
                }
                try {
                    byte[] bytes = HexUtil.parseHex(calibration.getPersonalAndCalibrationString());
                } catch (Exception e) {
                    return PageResp.fail("第 " + rowIndex + " 行标定数据解析错误！请检查数据是否正常！");
                }

            }
            for (DkmPhoneCalibrationData calibrationData : calibrationList) {

                // 查询已经存在的手机标定数据
                LambdaQueryWrapper<DkmPhoneCalibrationData> queryWrapper = Wrappers.<DkmPhoneCalibrationData>lambdaQuery()
                        .eq(DkmPhoneCalibrationData::getVehicleModel, calibrationData.getVehicleModel())
                        .eq(DkmPhoneCalibrationData::getVehicleBrand, calibrationData.getVehicleBrand())
                        .eq(DkmPhoneCalibrationData::getVehicleType, calibrationData.getVehicleType())
                        .eq(DkmPhoneCalibrationData::getPhoneModel, calibrationData.getPhoneModel());

                DkmPhoneCalibrationData alreadyExist = dkmPhoneCalibrationDataMapper.selectOne(queryWrapper);
                if (alreadyExist != null) {
                    dkmPhoneCalibrationDataMapper.delete(queryWrapper);
                }
                // 如果手机品牌和手机型号为default则为默认标定数据，需要放redis，再放数据库
                if (Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getPhoneBrand()) && Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getPhoneModel())) {
                    redisUtils.setCacheObject(CalibrationDataConstant.DEFAULT, calibrationData.getPersonalAndCalibrationString());
                    DkmPhoneCalibrationData dkmPhoneCalibrationData = dkmPhoneCalibrationDataMapper.selectOne(new QueryWrapper<DkmPhoneCalibrationData>().lambda()
                            .eq(DkmPhoneCalibrationData::getPhoneModel, CalibrationDataConstant.DEFAULT)
                            .eq(DkmPhoneCalibrationData::getVehicleBrand, CalibrationDataConstant.DEFAULT)
                            .eq(DkmPhoneCalibrationData::getVehicleType, CalibrationDataConstant.DEFAULT)
                            .eq(DkmPhoneCalibrationData::getPhoneBrand, CalibrationDataConstant.DEFAULT));
                    if (dkmPhoneCalibrationData == null) {
                        // 插入一行
                        DkmPhoneCalibrationData phoneCalibrationData = new DkmPhoneCalibrationData();
                        phoneCalibrationData.setPhoneBrand(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setPhoneModel(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setVehicleBrand(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setVehicleType(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setPersonalAndCalibrationString(calibrationData.getPersonalAndCalibrationString());
                        phoneCalibrationData.setCreateTime(new Date());
                        dkmPhoneCalibrationDataMapper.insert(phoneCalibrationData);
                    } else {
                        // 更新
                        dkmPhoneCalibrationData.setPersonalAndCalibrationString(calibrationData.getPersonalAndCalibrationString());
                        dkmPhoneCalibrationData.setUpdateTime(new Date());
                        dkmPhoneCalibrationDataMapper.updateById(dkmPhoneCalibrationData);
                    }
                }
            }
            int successInsert = dkmPhoneCalibrationDataMapper.insertPhoneCalibrationDataBatch(calibrationList);
            if (successInsert != calibrationList.size()) {
                return PageResp.fail("导入失败，请检查数据是否正确！");
            }
            return PageResp.success("导入成功");
        } catch (Exception e) {
            return PageResp.fail("导入失败，请检查excel文件！");
        }
    }


    /**
     * 将标定字符串转换为十六进制字符串
     *
     * @param calibration 标定字符串
     * @return 十六进制字符串
     */
    private String parseCalibrationToHexString(String calibration) throws VecentException {
        String[] split = StrUtil.sub(calibration, 1, -1).split(",");
        StringBuilder result = new StringBuilder();
        for (String s : split) {
            int number = Integer.parseInt(StrUtil.trim(s));
            if (number > ExcelConstant.CALIBRATION_MAX || number < 0) {
                throw new VecentException("标定数据不合法，请检查数据是否正确！");
            }
            String hexString = Integer.toHexString(number);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            result.append(hexString);
        }
        return result.toString();
    }

    public void downloadCalibrationExcel(String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand, Boolean isXlsx, HttpServletResponse response) throws UnsupportedEncodingException {

        LambdaQueryWrapper<DkmPhoneCalibrationData> queryWrapper = Wrappers.<DkmPhoneCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmPhoneCalibrationData::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmPhoneCalibrationData::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleType), DkmPhoneCalibrationData::getVehicleType, vehicleType)
                .like(StrUtil.isNotBlank(phoneBrand), DkmPhoneCalibrationData::getPhoneBrand, phoneBrand);
        List<DkmPhoneCalibrationData> calibrationDataList = dkmPhoneCalibrationDataMapper.selectList(queryWrapper);

        if (isXlsx == null) {
            isXlsx = false;
        }

        String suffix = isXlsx ? ExcelConstant.EXCEL_SUFFIX_XLS : ExcelConstant.EXCEL_SUFFIX_XLSX;
        // 设置字符编码
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode("手机标定数据", "UTF-8") + suffix);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");


        ExcelWriter writer = ExcelUtil.getWriter(isXlsx);


        writer.setOnlyAlias(true);

        Font headFont = writer.createFont();
        Font cellFont = writer.createFont();

        cellFont.setFontName("宋体");
        headFont.setFontName("宋体");
        headFont.setBold(true);

        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setFont(headFont);
        headCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setFont(cellFont);

        writer.setColumnWidth(0, 20);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 30);
        writer.setColumnWidth(3, 30);
        writer.setColumnWidth(4, 30);
        writer.setColumnWidth(5, 150);

        writer.addHeaderAlias("vehicleModel", "车辆型号");
        writer.addHeaderAlias("phoneBrand", "手机品牌");
        writer.addHeaderAlias("phoneModel", "手机型号");
        writer.addHeaderAlias("vehicleBrand", "车辆品牌");
        writer.addHeaderAlias("vehicleType", "车型");
        writer.addHeaderAlias("personalAndCalibrationString", "标定数据");

        writer.write(calibrationDataList, true);

        try {
            writer.flush(response.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

    }
}
