package com.vecentek.back.controller;

import cn.hutool.core.util.IdUtil;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.service.impl.DkmKeyLogServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private DkmKeyLogMapper dkmKeyLogMapper;


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

    @GetMapping("/addTest")
    public void addTest(String operateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(operateTime);
        DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setId(IdUtil.getSnowflakeNextId());
        dkmKeyLog.setVin("1");
        dkmKeyLog.setKeyId("2");
        dkmKeyLog.setFlag(1);
        dkmKeyLog.setOperateTime(date);
        dkmKeyLog.setStatusCode("1");
        dkmKeyLogMapper.insert(dkmKeyLog);
    }

    /**
     * 开始导出钥匙记录excel
     *
     * @param vin
     * @param userId
     * @param startTime
     * @param endTime
     * @param phoneBrand
     * @param phoneModel
     * @param statusCode
     * @param vehicleBrand
     * @param vehicleModel
     * @param vehicleType
     * @param flag
     * @param creator
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
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

        downloadKeyLogExcel(vin, userId, startTime, endTime, phoneBrand, phoneModel, statusCode, flag, vehicleBrand, vehicleModel, vehicleType, creator);
        return PageResp.success("正在导出");

    }

    /**
     * 开启其他线程异步导出钥匙记录excel
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
    public void downloadKeyLogExcel(String vin,
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
                                    String creator
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
                        creator);

    }

    /**
     * 查询历史导出记录表
     *
     * @param creator
     * @param type
     * @return
     */
    @GetMapping("/checkKeyUseLog")
    public PageResp checkKeyUseLog(String creator, Integer type) {
        //查询历史导出记录表(倒排)
        return this.dkmKeyUseLogService.checkKeyUseLog(creator, type);
    }


}
