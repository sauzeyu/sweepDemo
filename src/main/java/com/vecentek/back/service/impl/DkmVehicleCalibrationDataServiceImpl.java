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
import com.vecentek.back.entity.DkmVehicleCalibrationData;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.mapper.DkmVehicleCalibrationDataMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * DkmPhoneCalibrationData 表服务实现类
 *
 * @author tangc
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@Service
public class DkmVehicleCalibrationDataServiceImpl {
    @Resource
    private DkmVehicleCalibrationDataMapper dkmVehicleCalibrationDataMapper;
    @Resource
    private RedisUtils redisUtils;


    /**
     * 查询分页数据
     *
     * @return 对象列表
     */
    public PageResp  selectForPage(int pageIndex, int pageSize, String vehicleModel,Integer level) throws DiagnosticLogsException {
        // 获取当前表中的总记录
        Page<DkmVehicleCalibrationData> page = new Page<>(pageIndex, pageSize);

        if (Objects.isNull(vehicleModel) || Objects.isNull(level)){
            throw new DiagnosticLogsException("24","5071");
        }
        LambdaQueryWrapper<DkmVehicleCalibrationData> queryWrapper = Wrappers.<DkmVehicleCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmVehicleCalibrationData::getVehicleModel, vehicleModel)
        .eq(level != null, DkmVehicleCalibrationData::getLevel, level);
        page = dkmVehicleCalibrationDataMapper.selectPage(page, queryWrapper);

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 通过主键更新单条手机标定数据
     *
     * @param
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateDkmVehicleCalibrationDataById(DkmVehicleCalibrationData dkmVehicleCalibrationData) throws DiagnosticLogsException {

        dkmVehicleCalibrationData.setUpdateTime(new Date());
        if (dkmVehicleCalibrationData.getVehicleAndCalibrationString().length() != ExcelConstant.CALIBRATION_LENGTH) {
            throw new DiagnosticLogsException("02","5052");
            //return PageResp.fail("标定数据必须是32字节");
        }
        if (!com.vecentek.back.util.HexUtil.isAsciiHexString(dkmVehicleCalibrationData.getVehicleAndCalibrationString())) {
            return PageResp.fail("标定数据解析错误！请检查数据是否正常！");
        }
        dkmVehicleCalibrationDataMapper.updateById(dkmVehicleCalibrationData);

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
            reader.addHeaderAlias("车型", "vehicleModel");
            reader.addHeaderAlias("蓝牙灵敏度等级", "level");
            reader.addHeaderAlias("标定数据", "vehicleAndCalibrationString");
            List<DkmVehicleCalibrationData> calibrationList = reader.readAll(DkmVehicleCalibrationData.class);
            reader.close();
            if (calibrationList.size() == 0) {
                return PageResp.fail("导入失败，请检查excel文件！");
            }
            int rowIndex = 1;
            for (DkmVehicleCalibrationData calibration : calibrationList) {
                rowIndex++;
                String vehicleAndCalibrationString = calibration.getVehicleAndCalibrationString().replace(" ", "");
                calibration.setVehicleAndCalibrationString(vehicleAndCalibrationString);
                calibration.setCreateTime(new Date());
                if (StringUtils.isBlank(calibration.getVehicleModel())) {
                    return PageResp.fail("第 " + rowIndex + " 行导入的车型数据不能为空！");
                }
                if (calibration.getVehicleAndCalibrationString().length() != ExcelConstant.CALIBRATION_LENGTH) {
                    throw new DiagnosticLogsException("02","5052");
                    //return PageResp.fail("第 " + rowIndex + " 行导入的标定数据必须是32字节！");
                }
                try {
                    byte[] bytes = HexUtil.parseHex(calibration.getVehicleAndCalibrationString());
                } catch (Exception e) {
                    return PageResp.fail("第 " + rowIndex + " 行标定数据解析错误！请检查数据是否正常！");
                }

            }
            //车型 + 蓝牙灵敏度 形成的联合主键放入set结构去重
            Set<String> hashSet = new HashSet<>();
            for (DkmVehicleCalibrationData calibrationData : calibrationList) {
                String afterID = calibrationData.getVehicleModel() + calibrationData.getLevel();
                if (!hashSet.add(afterID)) {
                    return PageResp.fail("车型【"+calibrationData.getVehicleModel() + "】与蓝牙灵敏度【" + calibrationData.getLevel() + "】有重复数据");
                }
                if (Integer.parseInt(calibrationData.getLevel()) > 20){
                    return PageResp.fail("请检查您的蓝牙灵敏度是否合规,蓝牙灵敏度最高为20");
                }

                // 查询已经存在的手机标定数据
                LambdaQueryWrapper<DkmVehicleCalibrationData> queryWrapper = Wrappers.<DkmVehicleCalibrationData>lambdaQuery()
                        .eq(DkmVehicleCalibrationData::getVehicleModel, calibrationData.getVehicleModel())
                        .eq(DkmVehicleCalibrationData::getLevel, calibrationData.getLevel());

                DkmVehicleCalibrationData alreadyExist = dkmVehicleCalibrationDataMapper.selectOne(queryWrapper);
                if (alreadyExist != null) {
                    dkmVehicleCalibrationDataMapper.delete(queryWrapper);
                }
                // 如果车型和蓝牙灵敏度等级为default则为默认标定数据，需要放redis，再放数据库
                if (Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getVehicleModel()) && Objects.equals(CalibrationDataConstant.DEFAULT, calibrationData.getLevel())) {
                    redisUtils.setCacheObject(CalibrationDataConstant.DEFAULT, calibrationData.getVehicleAndCalibrationString());
                    DkmVehicleCalibrationData dkmVehicleCalibrationData = dkmVehicleCalibrationDataMapper.selectOne(new QueryWrapper<DkmVehicleCalibrationData>().lambda()
                            .eq(DkmVehicleCalibrationData::getVehicleModel, CalibrationDataConstant.DEFAULT)
                            .eq(DkmVehicleCalibrationData::getLevel, CalibrationDataConstant.DEFAULT));
                    if (dkmVehicleCalibrationData == null) {
                        // 插入一行
                        DkmVehicleCalibrationData vehicleCalibrationData = new DkmVehicleCalibrationData();
                        vehicleCalibrationData.setVehicleModel(CalibrationDataConstant.DEFAULT);
                        vehicleCalibrationData.setLevel(CalibrationDataConstant.DEFAULT);
                        vehicleCalibrationData.setVehicleAndCalibrationString(calibrationData.getVehicleAndCalibrationString());
                        vehicleCalibrationData.setCreateTime(new Date());
                        dkmVehicleCalibrationDataMapper.insert(vehicleCalibrationData);
                    } else {
                        // 更新
                        dkmVehicleCalibrationData.setVehicleAndCalibrationString(calibrationData.getVehicleAndCalibrationString());
                        dkmVehicleCalibrationData.setUpdateTime(new Date());
                        dkmVehicleCalibrationDataMapper.updateById(dkmVehicleCalibrationData);
                    }
                }
            }
            int successInsert = dkmVehicleCalibrationDataMapper.insertPhoneCalibrationDataBatch(calibrationList);
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

    public void downloadCalibrationExcel(String vehicleModel, Integer level, Boolean isXlsx, HttpServletResponse response) throws UnsupportedEncodingException {

        LambdaQueryWrapper<DkmVehicleCalibrationData> queryWrapper = Wrappers.<DkmVehicleCalibrationData>lambdaQuery()
                .like(StrUtil.isNotBlank(vehicleModel), DkmVehicleCalibrationData::getVehicleModel, vehicleModel)
                .eq(level!= null, DkmVehicleCalibrationData::getLevel, level);
        List<DkmVehicleCalibrationData> calibrationDataList = dkmVehicleCalibrationDataMapper.selectList(queryWrapper);

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
        writer.setColumnWidth(5, 150);

        writer.addHeaderAlias("vehicleModel", "车型");
        writer.addHeaderAlias("level", "蓝牙灵敏度等级");
        writer.addHeaderAlias("vehicleAndCalibrationString", "标定数据");

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
