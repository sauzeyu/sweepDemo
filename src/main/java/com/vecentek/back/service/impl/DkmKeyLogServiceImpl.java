package com.vecentek.back.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.BluetoothErrorReasonEnum;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.constant.KeyErrorReasonEnum;
import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.common.response.PageResp;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-24 17:11
 */
@Service
public class DkmKeyLogServiceImpl {
    @Resource
    private DkmKeyLogMapper dkmKeyLogMapper;
    @Resource
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;


    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String userId, String startTime, String endTime, String phoneBrand, String phoneModel, String statusCode, Integer flag,String vehicle_brand,String vehicle_model) {
        Page<DkmKeyLog> page = new Page<>(pageIndex, pageSize);

        LambdaQueryWrapper<DkmKeyLog> wrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(StrUtil.isNotBlank(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .like(StrUtil.isNotBlank(vehicle_brand), DkmKeyLog::getVehicleBrand, vehicle_brand)
                .like(StrUtil.isNotBlank(vehicle_model), DkmKeyLog::getVehicleModel, vehicle_model)
                .like(StrUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .like(StrUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(StrUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .like(StrUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .eq(ObjectUtil.isNotNull(flag),DkmKeyLog::getFlag,flag)
                .ge(StrUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .orderByDesc(DkmKeyLog::getOperateTime);
        page = dkmKeyLogMapper.selectPage(page, wrapper);

        if (page.getRecords().size() > 0) {
            page.getRecords().forEach(keyLog -> {
                keyLog.setStatusCode(KeyStatusCodeEnum.matchName(keyLog.getStatusCode()));
                if (keyLog.getFlag() != null && keyLog.getFlag() == 0) {
                    if (KeyStatusCodeEnum.SAFE_BLUETOOTH_DISCONNECT.getName().equals(keyLog.getStatusCode())) {
                        keyLog.setErrorReason(BluetoothErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    } else {
                        keyLog.setErrorReason(KeyErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    }
                }
            });
        }

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    public static void main(String[] args) {



        // 1.3形成文件名
        String excelName ="2023-6-1--2022-7-12钥匙使用记录";

        // 1.4对xls.xlsx区分 确定文件后缀
        String suffix =  ExcelConstant.EXCEL_SUFFIX_XLS ;
        excelName = excelName + suffix;
        // 1.5 使用1.1处文件名(时间戳)进行文件命名 并指定到服务器路径
        BigExcelWriter writer = ExcelUtil.getBigWriter("d:\\"+ excelName);
        writer.write(new ArrayList<>());
        writer.flush();
        writer.close();
    }
    @Async
    public void downloadKeyLogExcel(String vin, String userId, String startTime, String endTime,
                                    String phoneBrand,String phoneModel,String statusCode,Integer flag,Boolean isXlsx) {

        // 异步导出如果没有选条件默认导出当前月份的数据
        if (CharSequenceUtil.isBlank(startTime) && CharSequenceUtil.isBlank(endTime)){
            String now = DateUtil.now();
            DateTime dateTime = new DateTime(now, DatePattern.NORM_DATETIME_FORMAT);
            int month = dateTime.getMonth() + 1;
            int nextMonth;
            if(month == 12){
                nextMonth = 1;
            } else {
                nextMonth = month + 1 ;
            }
            startTime = getFirstDayOfMonth(month);
            endTime = getFirstDayOfMonth(nextMonth);

        }
        // 1Excel 文件名 文件格式 文件路径的提前处理
        // 1.1时间格式化格式

        Date createTime = new Date();
        // 1.2导出的excel按月份以时间命名 如2022-6-1~2022-7-1钥匙使用记录
        DateTime startName = DateUtil.parse(startTime);
        String startFileName = DateUtil.format(startName, "yyyy-MM-dd");

        DateTime endName = DateUtil.parse(endTime);
        String endFileName = DateUtil.format(endName, "yyyy-MM-dd");
        String fileName = startFileName + "~" + endFileName;
        // 1.3形成文件名
        String excelName = fileName + "钥匙使用记录";
        if (isXlsx == null) {
            isXlsx = false;
        }
        // 1.4对xls.xlsx区分 确定文件后缀
        String suffix = isXlsx ? ExcelConstant.EXCEL_SUFFIX_XLS : ExcelConstant.EXCEL_SUFFIX_XLSX;

        // 1.5 使用1.1处文件名(时间戳)进行文件命名 并指定到服务器路径
        String filePath = ("d:/test/" +  excelName + suffix);
        // 是否有重名文件
        if (FileUtil.isFile(filePath)) {
            FileUtil.del(filePath);
        }
        BigExcelWriter writer = ExcelUtil.getBigWriter(filePath);




        // 2向历史导出记录新增一条状态为导出中的数据
        dkmKeyLogHistoryExportMapper.insert(new DkmKeyLogHistoryExport(0, excelName, null, null, null, createTime, null));


        // 3.1所有数据量
        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(CharSequenceUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .like(CharSequenceUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(CharSequenceUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .like(CharSequenceUtil.isNotBlank(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .eq(flag!= null, DkmKeyLog::getFlag, flag)
                .like(CharSequenceUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .ge(CharSequenceUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(CharSequenceUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime);
        Integer sum = dkmKeyLogMapper.selectCount(queryWrapper);
        // 3.2每次分页数据量50W (SXXSF 最大分页100W)
        Integer end = 500000;

        List<DkmKeyLog> dkmKeyLogs;


        // 4将数据库查询和单个sheet导出操作视为原子操作 按数据总量和递增值计算原子操作数
        // TODO stream流
        for (int i = 0; i <= sum / end; i++) {
            int start = (i * end);

            // 4.1分页查询数据 否则会OOM
            dkmKeyLogs = getDkmKeyLogs(vin,
                    userId,
                    startTime,
                    endTime,
                    phoneBrand,
                    phoneModel,
                    statusCode,
                    flag,
                    start,
                    end);

            // 4.2首个sheet需要重新命名
            if (i == 0) {
                writer.renameSheet("初始表");
                // 4.3写入新sheet会刷新样式 每次都需要重新设置单元格样式
                extracted(writer);
                // 4.4一次性写出内容，使用默认样式，强制输出标题
                writer.write(dkmKeyLogs, true);
                continue;
            }
            writer.setSheet("表" + (i + 1));
            extracted(writer);
            writer.write(dkmKeyLogs, true);
        }

        writer.close();

        // 5将历史记录中该条数据记录根据导出情况进行修改
        dkmKeyLogHistoryExportMapper.update(null,
                Wrappers.<DkmKeyLogHistoryExport>lambdaUpdate().set(DkmKeyLogHistoryExport::getExportStatus, 1).eq(DkmKeyLogHistoryExport::getMissionName, excelName));

    }

    /**
     * BigExcelWriter设置单元格样式
     * @param writer
     */
    private void extracted(BigExcelWriter writer) {
        // 4.3.1表头只显示取别名的字段
        writer.setOnlyAlias(true);

        // 4.3.2设置单元格与字体的样式
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

        writer.setColumnWidth(0, 30);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);
        writer.setColumnWidth(4, 20);
        writer.setColumnWidth(5, 20);
        writer.setColumnWidth(6, 20);
        writer.setColumnWidth(7, 20);


        writer.addHeaderAlias("vin", "车辆vin号");
        writer.addHeaderAlias("userId", "用户id");
        writer.addHeaderAlias("phoneModel", "手机型号");
        writer.addHeaderAlias("phoneBrand", "手机品牌");
        writer.addHeaderAlias("operateTime", "操作时间");
        writer.addHeaderAlias("statusCode", "操作类型");
        writer.addHeaderAlias("flag", "操作结果");
        writer.addHeaderAlias("errorReason", "失败原因");
    }

    /**
     * 根据分页条件去查询钥匙记录
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param flag
     * @param start
     * @param end
     * @return
     */
    private List<DkmKeyLog> getDkmKeyLogs(String vin,
                                          String userId,
                                          String startTime,
                                          String endTime,
                                          String phoneBrand,
                                          String phoneModel,
                                          String statusCode,
                                          Integer flag,
                                          Integer start,
                                          Integer end) {

        // 4.1.1根据条件查出库中对应记录数据
        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(CharSequenceUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .like(CharSequenceUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(CharSequenceUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .like(CharSequenceUtil.isNotBlank(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .eq(flag!= null, DkmKeyLog::getFlag, flag)
                .like(CharSequenceUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .ge(CharSequenceUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(CharSequenceUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .last("limit " + start + "," + end);
        List<DkmKeyLog> keyLogList = dkmKeyLogMapper.selectList(queryWrapper);

        // 4.1.2执行库中部分字段二次封装
        if (!keyLogList.isEmpty()) {

            keyLogList.forEach(keyLog -> {
                keyLog.setStatusCode(KeyStatusCodeEnum.matchName(keyLog.getStatusCode()));
                if (keyLog.getFlag() != null && keyLog.getFlag() == 0) {
                    if (KeyStatusCodeEnum.SAFE_BLUETOOTH_DISCONNECT.getName().equals(keyLog.getStatusCode())) {
                        keyLog.setErrorReason(BluetoothErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    } else {
                        keyLog.setErrorReason(KeyErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    }
                }
            });

        }

        return keyLogList;
    }

    /**
     * 获取当前月第一天
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime())+" 00:00:00";
    }
    public PageResp checkKeyUseLog() {
        List<DkmKeyLogHistoryExport> dkmKeyLogHistoryExports = dkmKeyLogHistoryExportMapper.selectList(null);
        return PageResp.success("查询成功", (long) dkmKeyLogHistoryExports.size(), dkmKeyLogHistoryExports);
    }
}