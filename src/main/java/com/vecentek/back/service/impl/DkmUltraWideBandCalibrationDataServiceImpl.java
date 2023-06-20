package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.CalibrationDataConstant;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.entity.DkmUltraWideBandCalibrationData;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.mapper.DkmUltraWideBandCalibrationDataMapper;
import com.vecentek.back.util.RedisUtils;
import com.vecentek.back.util.UploadUtil;
import com.vecentek.common.response.PageResp;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


/**
 * DkmUltraWideBandCalibrationData 表服务实现类
 *
 * @author tangc
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@Service
public class DkmUltraWideBandCalibrationDataServiceImpl {
    @Resource
    private DkmUltraWideBandCalibrationDataMapper DkmUltraWideBandCalibrationDataMapper;
    @Resource
    private RedisUtils redisUtils;


    /**
     * 查询分页数据
     *
     * @return 对象列表
     */
    public PageResp selectForPage(int pageIndex, int pageSize, String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand) {
        // 获取当前表中的总记录
        Page<DkmUltraWideBandCalibrationData> page = new Page<>(pageIndex, pageSize);

        LambdaQueryWrapper<DkmUltraWideBandCalibrationData> queryWrapper = Wrappers.<DkmUltraWideBandCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmUltraWideBandCalibrationData::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(phoneBrand), DkmUltraWideBandCalibrationData::getPhoneBrand, phoneBrand);
        page = DkmUltraWideBandCalibrationDataMapper.selectPage(page, queryWrapper);

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 通过主键更新单条手机标定数据
     *
     * @param DkmUltraWideBandCalibrationData 手机标定数据
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateDkmUltraWideBandCalibrationDataById(DkmUltraWideBandCalibrationData DkmUltraWideBandCalibrationData) throws DiagnosticLogsException {

        DkmUltraWideBandCalibrationData.setUpdateTime(new Date());
        if (DkmUltraWideBandCalibrationData.getPersonalAndCalibrationString().length() != ExcelConstant.CALIBRATION_LENGTH) {

            return PageResp.fail("标定数据必须是32字节");
        }
        if (!com.vecentek.back.util.HexUtil.isAsciiHexString(DkmUltraWideBandCalibrationData.getPersonalAndCalibrationString())) {
            return PageResp.fail("标定数据解析错误！请检查数据是否正常！");
        }
        DkmUltraWideBandCalibrationDataMapper.updateById(DkmUltraWideBandCalibrationData);

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
        // 创建Validator工厂
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        // 获取Validator实例
        Validator validator = factory.getValidator();

        try {
            PageResp pageResp = UploadUtil.checkFile(file);
            if (pageResp != null) {
                return pageResp;
            }
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            reader.addHeaderAlias("车辆型号", "vehicleModel");
            reader.addHeaderAlias("手机品牌", "phoneBrand");
            reader.addHeaderAlias("手机型号", "phoneModel");
            //reader.addHeaderAlias("车辆品牌", "vehicleBrand");
            //reader.addHeaderAlias("车型", "vehicleType");
            reader.addHeaderAlias("标定数据", "personalAndCalibrationString");
            reader.addHeaderAlias("特征点数据", "featureData");
            List<DkmUltraWideBandCalibrationData> calibrationList = reader.readAll(DkmUltraWideBandCalibrationData.class);
            reader.close();
            if (calibrationList.size() == 0) {
                return PageResp.fail("导入失败，请检查excel文件！");
            }
            int rowIndex = 1;
            //车辆型号 + 手机型号 形成的联合主键放入set结构去重
            Set<String> hashSet = new HashSet<>();
            for (DkmUltraWideBandCalibrationData calibration : calibrationList) {

                // 使用Validator的validate方法校验单个实体
                Set<ConstraintViolation<DkmUltraWideBandCalibrationData>> violations  = validator.validate(calibration);

                // 如果校验不通过，则打印错误信息
                if (violations.size() > 0) {
                    System.out.println("Error occurred at index: " + rowIndex);
                    for (ConstraintViolation<DkmUltraWideBandCalibrationData> violation : violations) {
                        return PageResp.fail("第"+ rowIndex + "行: " + violation.getMessage());
                    }
                }

                String afterID = calibration.getVehicleModel() + calibration.getPhoneModel();
                if (!hashSet.add(afterID)) {
                    return PageResp.fail("车辆型号【" + calibration.getVehicleModel() + "】与手机型号【" + calibration.getPhoneModel() + "】有重复数据");
                }
                rowIndex++;
                String personalAndCalibrationString = calibration.getPersonalAndCalibrationString().replace(" ", "");
                calibration.setPersonalAndCalibrationString(personalAndCalibrationString);
                calibration.setCreateTime(new Date());

                if (!com.vecentek.back.util.HexUtil.isAsciiHexString(calibration.getPersonalAndCalibrationString())) {
                    return PageResp.fail("第 " + rowIndex + " 行标定数据解析错误！请检查数据是否正常！");
                }


            }




            for (DkmUltraWideBandCalibrationData calibrationData : calibrationList) {

                // 查询已经存在的手机标定数据
                LambdaQueryWrapper<DkmUltraWideBandCalibrationData> queryWrapper = Wrappers.<DkmUltraWideBandCalibrationData>lambdaQuery()
                        .eq(DkmUltraWideBandCalibrationData::getVehicleModel, calibrationData.getVehicleModel())
                        .eq(DkmUltraWideBandCalibrationData::getPhoneModel, calibrationData.getPhoneModel());

                DkmUltraWideBandCalibrationData alreadyExist = DkmUltraWideBandCalibrationDataMapper.selectOne(queryWrapper);
                if (alreadyExist != null) {
                    DkmUltraWideBandCalibrationDataMapper.delete(queryWrapper);
                }
                // 如果手机品牌和手机型号为default则为默认标定数据，需要放redis，再放数据库
                if (Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getPhoneBrand()) && Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getPhoneModel())) {
                    redisUtils.setCacheObject(CalibrationDataConstant.DEFAULT, calibrationData.getPersonalAndCalibrationString());
                    DkmUltraWideBandCalibrationData dkmUltraWideBandCalibrationData = DkmUltraWideBandCalibrationDataMapper.selectOne(new QueryWrapper<DkmUltraWideBandCalibrationData>().lambda()
                            .eq(DkmUltraWideBandCalibrationData::getPhoneModel, CalibrationDataConstant.DEFAULT));
                    if (dkmUltraWideBandCalibrationData == null) {
                        // 插入一行
                        DkmUltraWideBandCalibrationData phoneCalibrationData = new DkmUltraWideBandCalibrationData();
                        phoneCalibrationData.setPhoneBrand(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setPhoneModel(CalibrationDataConstant.DEFAULT);
                        phoneCalibrationData.setPersonalAndCalibrationString(calibrationData.getPersonalAndCalibrationString());
                        phoneCalibrationData.setCreateTime(new Date());
                        DkmUltraWideBandCalibrationDataMapper.insert(phoneCalibrationData);
                    } else {
                        // 更新
                        dkmUltraWideBandCalibrationData.setPersonalAndCalibrationString(calibrationData.getPersonalAndCalibrationString());
                        dkmUltraWideBandCalibrationData.setUpdateTime(new Date());
                        DkmUltraWideBandCalibrationDataMapper.updateById(dkmUltraWideBandCalibrationData);
                    }
                }
            }
            int successInsert = DkmUltraWideBandCalibrationDataMapper.insertPhoneCalibrationDataBatch(calibrationList);
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

        LambdaQueryWrapper<DkmUltraWideBandCalibrationData> queryWrapper = Wrappers.<DkmUltraWideBandCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmUltraWideBandCalibrationData::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(phoneBrand), DkmUltraWideBandCalibrationData::getPhoneBrand, phoneBrand);
        List<DkmUltraWideBandCalibrationData> calibrationDataList = DkmUltraWideBandCalibrationDataMapper.selectList(queryWrapper);

        if (isXlsx == null) {
            isXlsx = false;
        }

        String suffix = isXlsx ? ExcelConstant.EXCEL_SUFFIX_XLS : ExcelConstant.EXCEL_SUFFIX_XLSX;
        // 设置字符编码
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode("UWB标定数据", "UTF-8") + suffix);
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

        //writer.setColumnWidth(0, 20);
        //writer.setColumnWidth(1, 20);
        //writer.setColumnWidth(2, 30);
        //writer.setColumnWidth(3, 30);
        //writer.setColumnWidth(4, 30);
        //writer.setColumnWidth(5, 150);
        //writer.setColumnWidth(6, 255);

        writer.setColumnWidth(0, 20);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 30);
        writer.setColumnWidth(3, 150);


        writer.addHeaderAlias("vehicleModel", "车辆型号");
        writer.addHeaderAlias("phoneBrand", "手机品牌");
        writer.addHeaderAlias("phoneModel", "手机型号");
        //writer.addHeaderAlias("vehicleBrand", "车辆品牌");
        //writer.addHeaderAlias("vehicleType", "车型");
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
