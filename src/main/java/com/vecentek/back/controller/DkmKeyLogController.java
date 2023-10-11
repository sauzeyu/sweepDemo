package com.vecentek.back.controller;

import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.service.impl.DkmKeyLogServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-25 13:40
 */
@RestController
@RequestMapping("/dkmKeyLog")

public class DkmKeyLogController {

    @Resource
    private DkmKeyLogServiceImpl dkmKeyUseLogService;
    @Resource
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;

    @GetMapping("/selectKeyStatusCode")
    public PageResp selectKeyStatusCode() {
        List<Map<String, String>> KeyStatusCodeVOList = new ArrayList<>();

        for (KeyStatusCodeEnum statusCode : KeyStatusCodeEnum.values()) {
            HashMap<String, String> KeyStatusCodeVO = new HashMap<>();
            KeyStatusCodeVO.put("code", statusCode.getCode());
            KeyStatusCodeVO.put("name", statusCode.getName());
            KeyStatusCodeVOList.add(KeyStatusCodeVO);
        }
        return PageResp.success("查询成功", KeyStatusCodeVOList);
    }


    @GetMapping("/selectForPage")
    public PageResp selectForPage(@RequestParam("pageIndex") int pageIndex,
                                  @RequestParam("pageSize") int pageSize,
                                  String userId,
                                  String vin,
                                  String startTime,
                                  String endTime,
                                  String phoneBrand,
                                  String phoneModel,
                                  @RequestParam(value = "statusCode", required = false) List<String> statusCode,
                                  Integer flag,
                                  String vehicleBrand,
                                  String vehicleModel,
                                  String vehicleType) {
        return dkmKeyUseLogService.selectForPage(pageIndex,
                pageSize,
                vin,
                userId,
                startTime,
                endTime,
                phoneBrand,
                phoneModel,
                statusCode,
                flag,
                vehicleBrand,
                vehicleModel,
                vehicleType);
    }


    /**
     * 开始导出钥匙记录excel
     *
     * @param vin          车架号
     * @param userId       用户Id
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param phoneBrand   手机品牌
     * @param phoneModel   手机型号
     * @param statusCode   状态码
     * @param vehicleBrand 车辆品牌
     * @param vehicleModel 车辆型号
     * @param vehicleType  车辆类型
     * @param flag         信号量
     * @param creator      创建者
     * @return PageResp
     */
    @PostMapping(value = "/downloadKeyLogExcel")
    public PageResp startLoadKeyLogExcel(String vin,
                                         String userId,
                                         String startTime,
                                         String endTime,
                                         String phoneBrand,
                                         String phoneModel,
                                         @RequestParam(value = "statusCode", required = false) List<String> statusCode,
                                         String vehicleBrand,
                                         String vehicleModel,
                                         String vehicleType,
                                         Integer flag,
                                         String creator
    ) {
        // 1.1 形成文件名
        String excelName = "钥匙使用记录-" + System.currentTimeMillis();

        // 2 向历史导出记录新增一条状态为导出中的数据
        DkmKeyLogHistoryExport build = DkmKeyLogHistoryExport.builder()
                .exportStatus(0)
                .missionName(excelName)
                .createTime(new Date())
                .creator(creator)
                .type(0)
                .build();

        dkmKeyLogHistoryExportMapper.insert(build);
        downloadKeyLogExcel(vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag, vehicleBrand, vehicleModel, vehicleType, excelName);
        return PageResp.success("正在导出");

    }

    /**
     * 开启其他线程异步导出钥匙记录excel
     *
     * @param vin          车架号
     * @param userId       用户Id
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param phoneBrand   手机品牌
     * @param phoneModel   手机型号
     * @param statusCode   状态码
     * @param vehicleBrand 车辆品牌
     * @param vehicleModel 车辆型号
     * @param vehicleType  车辆类型
     * @param flag         信号量
     */
    @Async
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
                                    String excelName
    ) {


        this.dkmKeyUseLogService.downloadKeyLogExcel
                (vin,
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
                        excelName
                );

    }


}
