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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
                                  String userId,
                                  String vin,
                                  String startTime,
                                  String endTime,
                                  String phoneBrand,
                                  String phoneModel,
                                  String statusCode,
                                  Integer flag,
                                  String vehicleBrand,
                                  String vehicleModel) {
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
                vehicleModel);
    }
    /**
     * 查询所有code
     *
     * @return 角色列表
     */
    @GetMapping(value = "/selectAllCode")
    public PageResp selectAllCode() {
        return this.dkmKeyUseLogService.selectAllCode();
    }
    /**
     * 开始导出
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param flag
     * @return
     */
    @PostMapping(value = "/downloadKeyLogExcel")
    public PageResp startLoadKeyLogExcel(String vin,
                                         String userId,
                                         String startTime,
                                         String endTime,
                                         String phoneBrand,
                                         String phoneModel,
                                         String statusCode,
                                         String vehicleBrand,
                                         String vehicleModel,
                                         Integer flag,
                                         String creator
    ) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //String token = httpServletRequest.getHeader("access-token");
        downloadKeyLogExcel(vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag, vehicleBrand, vehicleModel,  creator);
        return PageResp.success("正在导出");

    }

    /**
     * 异步导出
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param flag
     */
    @Async
    public void downloadKeyLogExcel(String vin,
                                    String userId,
                                    String startTime,
                                    String endTime,
                                    String phoneBrand,
                                    String phoneModel,
                                    String statusCode,
                                    Integer flag,
                                    String vehicleBrand,
                                    String vehicleModel,
                                    String creator
    ) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {


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
                creator);

    }

    @GetMapping("/checkKeyUseLog")
    public PageResp checkKeyUseLog( String creator,Integer type) {
        //查询历史导出记录表(倒排)
        return this.dkmKeyUseLogService.checkKeyUseLog(creator,type);
    }

}
