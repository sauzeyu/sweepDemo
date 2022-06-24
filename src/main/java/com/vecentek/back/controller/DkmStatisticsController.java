package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmStatisticsServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-10 14:59
 */

@RestController
@RequestMapping(value = "/dkmStatistics")
public class DkmStatisticsController {

    @Resource
    private DkmStatisticsServiceImpl echartsServiceImpl;

    @RequestMapping(value = "/selectTotal", method = RequestMethod.GET)
    public PageResp selectUserTotal(String startTime, String endTime) {
        return echartsServiceImpl.selectTotal(startTime, endTime);
    }

    @RequestMapping(value = "/selectKeyLogByMonth", method = RequestMethod.GET)
    public PageResp selectKeyLogByMonth() {
        return echartsServiceImpl.selectKeyLogByMonth();
    }

    @RequestMapping(value = "/selectKeyUseLogByTime", method = RequestMethod.GET)
    public PageResp selectKeyUseLogByTime(@RequestParam String startTime, @RequestParam String endTime) {
        return echartsServiceImpl.selectKeyUseLogByTime(startTime, endTime);
    }

    @RequestMapping(value = "/selectKeyErrorLogByTime", method = RequestMethod.GET)
    public PageResp selectKeyErrorLogByTime(@RequestParam String startTime, @RequestParam String endTime) {
        return echartsServiceImpl.selectKeyErrorLogByTime(startTime, endTime);
    }

    /**
     * 根据手机品牌查询对应错误日志占比
     * @param phoneBrand
     * @return
     */
    @RequestMapping(value = "/selectKeyErrorLogByPhoneBrand", method = RequestMethod.GET)
    public PageResp selectKeyErrorLogByPhoneBrand(@RequestParam String phoneBrand) {
        return echartsServiceImpl.selectKeyErrorLogByPhoneBrand(phoneBrand);
    }

    /**
     * 车辆总数统计框
     * 按一年分成12个月查
     * 每日新增车辆数
     * @return
     */
    @RequestMapping(value = "/vehicleStatistics", method = RequestMethod.GET)
    public PageResp vehicleStatistics() {
        return echartsServiceImpl.vehicleStatistics();
    }

    /**
     * 钥匙总量统计框
     * 子钥匙，车主钥匙，车主钥匙占比
     * @return
     */
    @RequestMapping(value = "/keyStatistics", method = RequestMethod.GET)
    public PageResp keyStatistics() {
        return echartsServiceImpl.keyStatistics();
    }

    /**
     * 钥匙使用次数
     * 子钥匙，车主钥匙，车主钥匙占比
     * @return
     */
    @RequestMapping(value = "/keyUseTimeStatistics", method = RequestMethod.GET)
    public PageResp keyUseTimeStatistics() {
        return echartsServiceImpl.keyStatistics();
    }

    /**
     * 按照时间段统计不同型号手机失败率、不同车型失败率、不同模块失败率
     * 不同手机型号的故障率,不同车型的故障率,不同故障码的故障率
     * @return
     */
    @RequestMapping(value = "/selectErrorStatusTotal", method = RequestMethod.POST)
    public PageResp selectErrorStatusTotal(@RequestParam String startTime, @RequestParam String endTime) {
        return echartsServiceImpl.selectErrorStatusTotal(startTime, endTime);
    }


}