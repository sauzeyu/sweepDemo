package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmKeyLogServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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


    @GetMapping("/selectForPage")
    public PageResp selectForPage(@RequestParam("pageIndex") int pageIndex,
                                  @RequestParam("pageSize") int pageSize,
                                  String vin,
                                  String userId,
                                  String startTime,
                                  String endTime,
                                  String phoneBrand,
                                  String phoneModel,
                                  String statusCode,
                                  Integer flag,
                                  String vehicle_brand,
                                  String vehicle_model) {
        return dkmKeyUseLogService.selectForPage(pageIndex, pageSize, vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag,vehicle_brand,vehicle_model);
    }

    @PostMapping(value = "/downloadKeyLogExcel")
    public PageResp startLoadKeyLogExcel(String vin,
                                         String userId,
                                         String startTime,
                                         String endTime,
                                         String phoneBrand,
                                         String phoneModel,
                                         String statusCode,
                                         Integer flag,
                                         Boolean isXlsx)  {

        downloadKeyLogExcel(vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag, isXlsx);
        return PageResp.success();

    }

    @Async
    public void downloadKeyLogExcel(String vin,
                                    String userId,
                                    String startTime,
                                    String endTime,
                                    String phoneBrand,
                                    String phoneModel,
                                    String statusCode,
                                    Integer flag,
                                    Boolean isXlsx) {
        this.dkmKeyUseLogService.downloadKeyLogExcel(vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag, isXlsx);

    }

    @GetMapping("/checkKeyUseLog")
    public PageResp checkKeyUseLog() {
        //查询历史导出记录表(倒排)
        return this.dkmKeyUseLogService.checkKeyUseLog();

    }

}
