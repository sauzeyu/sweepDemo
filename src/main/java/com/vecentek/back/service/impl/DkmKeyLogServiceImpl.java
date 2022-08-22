package com.vecentek.back.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
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
import com.vecentek.back.util.DownLoadUtil;
import com.vecentek.common.response.PageResp;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
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


    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String userId, String startTime, String endTime, String phoneBrand, String phoneModel, List<String> statusCode, Integer flag, String vehicleBrand, String vehicleModel, String vehicleType) {
        Page<DkmKeyLog> page = new Page<>(pageIndex, pageSize);
        //1Excel 文件名 文件格式 文件路径的提前处理 例如2022-6-1~2022-7-1钥匙使用记录
        if (
            //CharSequenceUtil.isBlank(vin)
            //&& CharSequenceUtil.isBlank(userId)

                CharSequenceUtil.isBlank(startTime)
                        && CharSequenceUtil.isBlank(endTime)

            //&& CharSequenceUtil.isBlank(phoneBrand)
            //&& CharSequenceUtil.isBlank(phoneModel)
            //&& ObjectUtil.isNull(statusCode)
            //&& flag == null
            //&& CharSequenceUtil.isBlank(vehicleBrand)
            //&& CharSequenceUtil.isBlank(vehicleModel)
            //&& CharSequenceUtil.isBlank(vehicleType)

        ) {
            //List<String> timeList = DownLoadUtil.checkLastWeekTotal(startTime, endTime, null);
            //startTime = timeList.get(FileConstant.STARTTIME);
            //endTime = timeList.get(FileConstant.ENDTIME);
            startTime = DownLoadUtil.getSysDate();
            endTime = DownLoadUtil.getNextDay();

        }


        LambdaQueryWrapper<DkmKeyLog> wrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                //.like(StrUtil.isNotBlank(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .in(ObjectUtil.isNotNull(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmKeyLog::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleModel), DkmKeyLog::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleType), DkmKeyLog::getVehicleType, vehicleType)
                .like(StrUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .like(StrUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(StrUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .like(StrUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .eq(ObjectUtil.isNotNull(flag), DkmKeyLog::getFlag, flag)
                .ge(StrUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .orderByDesc(DkmKeyLog::getOperateTime);
        page = dkmKeyLogMapper.selectPage(page, wrapper);

        if (page.getRecords().size() > 0) {
            page.getRecords().forEach(keyLog -> {
                keyLog.setOperationType(KeyStatusCodeEnum.matchName(keyLog.getStatusCode()));
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

    /**
     * 异步下载单表分页钥匙记录信息
     *
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param flag
     * @param vehicleBrand
     * @param vehicleModel
     * @param vehicleType
     * @param creator
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void downloadKeyLogExcel(String vin,
                                    String userId,
                                    String startTime,
                                    String endTime,
                                    String phoneBrand,
                                    String phoneModel,
                                    List<String> statusCode,
                                    Integer flag,
                                    String vehicleBrand,
                                    String vehicleModel,
                                    String vehicleType,
                                    String creator) {

        // 1.1 形成文件名
        String excelName = "钥匙使用记录-" + System.currentTimeMillis();

        // 1.2 使用1处文件名(时间戳)进行文件命名 并指定到服务器路径
        String filePath = ("/excel/" + excelName + ExcelConstant.EXCEL_SUFFIX_XLSX);

        // 是否有重名文件
        if (FileUtil.isFile(filePath)) {
            FileUtil.del(filePath);
        }


        ExcelWriter writer = ExcelUtil.getWriter(filePath);


        // 2 向历史导出记录新增一条状态为导出中的数据
        DkmKeyLogHistoryExport build = DkmKeyLogHistoryExport.builder()
                .exportStatus(0)
                .missionName(excelName)
                .createTime(new Date())
                .creator(creator)
                .type(0)
                .build();

        dkmKeyLogHistoryExportMapper.insert(build);


        // 3.1所有数据量
        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(CharSequenceUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .like(CharSequenceUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(CharSequenceUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .in(ObjectUtil.isNotNull(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmKeyLog::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleModel), DkmKeyLog::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleType), DkmKeyLog::getVehicleType, vehicleType)
                .eq(flag != null, DkmKeyLog::getFlag, flag)
                .like(CharSequenceUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .ge(CharSequenceUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(CharSequenceUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime);
        Integer sum = dkmKeyLogMapper.selectCount(queryWrapper);
        // 3.2每次分页数据量10W (SXXSF 最大分页100W)
        Integer end = 100000;

        List<DkmKeyLog> dkmKeyLogs;


        // 4 将数据库查询和单个sheet导出操作视为原子操作 按数据总量和递增值计算原子操作数
        try {
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
                        vehicleBrand,
                        vehicleModel,
                        vehicleType,
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

        // 5将历史记录中该条数据记录根据导出情况进行修改
        dkmKeyLogHistoryExportMapper.update(null,
                Wrappers.<DkmKeyLogHistoryExport>lambdaUpdate().set(DkmKeyLogHistoryExport::getExportStatus, 1).eq(DkmKeyLogHistoryExport::getMissionName, excelName));

    }

    /**
     * BigExcelWriter设置单元格样式
     *
     * @param writer
     */
    private void extracted(ExcelWriter writer) {
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
        writer.setColumnWidth(8, 20);


        writer.addHeaderAlias("vin", "车辆vin号");
        writer.addHeaderAlias("userId", "用户id");
        writer.addHeaderAlias("phoneBrand", "手机品牌");
        writer.addHeaderAlias("phoneModel", "手机型号");
        writer.addHeaderAlias("vehicleBrand", "车辆品牌");
        writer.addHeaderAlias("vehicleModel", "车辆型号");


        writer.addHeaderAlias("operateTime", "操作时间");


        writer.addHeaderAlias("statusCode", "操作码");
        writer.addHeaderAlias("operationType", "操作类型");
        writer.addHeaderAlias("flag", "操作结果");
        writer.addHeaderAlias("errorReason", "失败原因");

    }

    /**
     * 根据分页条件去查询钥匙记录
     *
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param flag
     * @param vehicleBrand
     * @param vehicleModel
     * @param vehicleType
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
                                          @RequestParam(value = "statusCode", required = false) List<String> statusCode,
                                          Integer flag,
                                          String vehicleBrand,
                                          String vehicleModel,
                                          String vehicleType,
                                          Integer start,
                                          Integer end) {

        // 4.1.1根据条件查出库中对应记录数据
        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(CharSequenceUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .like(CharSequenceUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(CharSequenceUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .in(ObjectUtil.isNotNull(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmKeyLog::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleModel), DkmKeyLog::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleType), DkmKeyLog::getVehicleType, vehicleType)
                .eq(flag != null, DkmKeyLog::getFlag, flag)
                .like(CharSequenceUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .ge(CharSequenceUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(CharSequenceUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .last("limit " + start + "," + end);
        List<DkmKeyLog> keyLogList = dkmKeyLogMapper.selectList(queryWrapper);

        // 4.1.2执行库中部分字段二次封装
        if (!keyLogList.isEmpty()) {

            keyLogList.forEach(keyLog -> {
                keyLog.setOperationType(KeyStatusCodeEnum.matchName(keyLog.getStatusCode()));
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
     * 根据具体用户和excel类型查询历史下载记录列表
     *
     * @param creator
     * @param type
     * @return
     */
    public PageResp checkKeyUseLog(String creator, Integer type) {
        LambdaQueryWrapper<DkmKeyLogHistoryExport> dkmKeyLogHistoryExportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dkmKeyLogHistoryExportLambdaQueryWrapper.eq(creator != null, DkmKeyLogHistoryExport::getCreator, creator)
                .eq(type != null, DkmKeyLogHistoryExport::getType, type)
                .orderByDesc(DkmKeyLogHistoryExport::getCreateTime)
        ;
        List<DkmKeyLogHistoryExport> dkmKeyLogHistoryExports = dkmKeyLogHistoryExportMapper.selectList(dkmKeyLogHistoryExportLambdaQueryWrapper);
        return PageResp.success("查询成功", (long) dkmKeyLogHistoryExports.size(), dkmKeyLogHistoryExports);
    }

}