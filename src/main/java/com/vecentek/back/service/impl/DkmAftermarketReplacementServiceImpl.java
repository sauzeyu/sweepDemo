package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.constant.FileConstant;
import com.vecentek.back.entity.DkmAftermarketReplacement;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmAftermarketReplacementMapper;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.util.DownLoadUtil;
import com.vecentek.common.response.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-16 14:54
 */
@Service
public class DkmAftermarketReplacementServiceImpl {
    @Resource
    private DkmAftermarketReplacementMapper dkmAftermarketReplacementMapper;
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;


    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String startTime, String endTime) {
        // 清洗
        if (StringUtils.isNotBlank(vin)) {
            vin = vin.trim();
        }
        Page<DkmAftermarketReplacement> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmAftermarketReplacement> queryWrapper = Wrappers.<DkmAftermarketReplacement>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmAftermarketReplacement::getVin, vin)
                .ge(StrUtil.isNotBlank(startTime), DkmAftermarketReplacement::getReplacementTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmAftermarketReplacement::getReplacementTime, endTime)
                .orderByDesc(DkmAftermarketReplacement::getReplacementTime);
        page = dkmAftermarketReplacementMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    public PageResp selectByVin(int pageIndex, int pageSize, String vin) {
        Page<DkmAftermarketReplacement> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmAftermarketReplacement> queryWrapper = Wrappers.<DkmAftermarketReplacement>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmAftermarketReplacement::getVin, vin)
                .orderByDesc(DkmAftermarketReplacement::getReplacementTime);
        page = dkmAftermarketReplacementMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    public PageResp selectVehicleByVin(String vin) {
        LambdaQueryWrapper<DkmVehicle> wrapper = Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, vin);
        DkmVehicle dkmVehicle = dkmVehicleMapper.selectOne(wrapper);
        return PageResp.success("查询成功", dkmVehicle);
    }

    /**
     * 下载换件信息excel
     *
     * @param vin
     * @param startTime
     * @param endTime
     * @param isXls
     * @param creator
     * @param response
     * @throws UnsupportedEncodingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void downloadAftermarketReplacement(String vin, String startTime, String endTime, Boolean isXls, String creator, HttpServletResponse response) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(vin)) {
            vin = vin.trim();
        }
        //1Excel 文件名 文件格式 文件路径的提前处理 例如2022-6-1~2022-7-1钥匙使用记录
        List<String> timeList = DownLoadUtil.checkLastWeekTotal(startTime, endTime, creator);
        String fileName = timeList.get(FileConstant.FILENAME);
        // 1.3形成文件名
        String excelName = fileName + "换件信息";

        LambdaQueryWrapper<DkmAftermarketReplacement> queryWrapper = Wrappers.<DkmAftermarketReplacement>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmAftermarketReplacement::getVin, vin)
                .ge(StrUtil.isNotBlank(startTime), DkmAftermarketReplacement::getReplacementTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmAftermarketReplacement::getReplacementTime, endTime)
                .orderByDesc(DkmAftermarketReplacement::getReplacementTime);
        List<DkmAftermarketReplacement> replacementDataList = dkmAftermarketReplacementMapper.selectList(queryWrapper);
        String suffix = null;
        if (isXls == null) {
            isXls = false;
        } else if (isXls) {
            suffix = ExcelConstant.EXCEL_SUFFIX_XLS;
        } else {
            suffix = ExcelConstant.EXCEL_SUFFIX_XLSX;
        }


        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(excelName, "UTF-8") + suffix);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");


        ExcelWriter writer = ExcelUtil.getWriter(!isXls);


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

        writer.setColumnWidth(0, 25);
        writer.setColumnWidth(1, 45);
        writer.setColumnWidth(2, 45);
        writer.setColumnWidth(3, 30);

        writer.addHeaderAlias("vin", "车辆vin号");
        writer.addHeaderAlias("oldBluetoothSn", "旧蓝牙设备序列号");
        writer.addHeaderAlias("newBluetoothSn", "新蓝牙设备序列号");
        writer.addHeaderAlias("replacementTime", "换件时间");

        writer.write(replacementDataList, true);

        try {
            writer.flush(response.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        // 2向历史导出记录新增一条状态为导出中的数据
        DkmKeyLogHistoryExport build = DkmKeyLogHistoryExport.builder()
                .exportStatus(1)
                .createTime(new Date())
                .missionName(excelName)
                .creator(creator)
                .build();
        dkmKeyLogHistoryExportMapper.insert(build);

    }
}
