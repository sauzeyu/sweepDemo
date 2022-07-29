package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmStatisticsServiceImpl;
import com.vecentek.back.vo.TimeQuantumVO;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * 面向TSP
     *
     * @param timeQuantumVO
     * @return
     */
    @PostMapping("/selectTotal")
    public PageResp selectUserTotal(@RequestBody TimeQuantumVO timeQuantumVO) {
        return echartsServiceImpl.selectTotal(timeQuantumVO.getStartTime(), timeQuantumVO.getEndTime());
    }

    /**
     * @return {@link PageResp}
     * @author EdgeYu
     * @date 2022-06-28 15:56
     */
    @GetMapping("/selectVehicleAndKeyAndKeyLogTotal")
    public PageResp selectVehicleAndKeyAndKeyLogTotal() {
        return echartsServiceImpl.selectVehicleAndKeyAndKeyLogTotal();
    }

    @GetMapping("/selectKeyLogByMonth")
    public PageResp selectKeyLogByMonth() {
        return echartsServiceImpl.selectKeyLogByMonth();
    }

    @GetMapping("/selectKeyUseLogByTime")
    public PageResp selectKeyUseLogByTime(@RequestParam String startTime, @RequestParam String endTime) {
        return echartsServiceImpl.selectKeyUseLogByTime(startTime, endTime);
    }

    @GetMapping("/selectKeyErrorLogByTime")
    public PageResp selectKeyErrorLogByTime(@RequestParam String startTime, @RequestParam String endTime) {
        return echartsServiceImpl.selectKeyErrorLogByTime(startTime, endTime);
    }

    /**
     * 根据各手机品牌查询对应错误日志占比
     *
     * @param
     * @return
     */
    @GetMapping("/selectKeyErrorLogByAllPhoneBrand")
    public PageResp selectKeyErrorLogByAllPhoneBrand() {
        return echartsServiceImpl.selectKeyErrorLogByAllPhoneBrand();
    }

    /**
     * 根据手机品牌查询对应错误日志占比
     *
     * @param phoneBrand
     * @return
     */
    @GetMapping("/selectKeyErrorLogByPhoneBrand")
    public PageResp selectKeyErrorLogByPhoneBrand(String phoneBrand) {
        return echartsServiceImpl.selectKeyErrorLogByPhoneBrand(phoneBrand);
    }

    /**
     * 车辆总数统计框
     * 按一年分成12个月查
     * 每日新增车辆数
     *
     * @return
     */
    @GetMapping(value = "/vehicleStatistics")
    public PageResp vehicleStatistics() {
        return echartsServiceImpl.vehicleStatistics();
    }

    /**
     * 钥匙总量统计框
     * 子钥匙，车主钥匙，车主钥匙占比
     *
     * @return
     */
    @GetMapping(value = "/keyStatistics")
    public PageResp keyStatistics() {
        return echartsServiceImpl.keyStatistics();
    }

    /**
     * 钥匙使用次数
     * 子钥匙，车主钥匙，车主钥匙占比
     *
     * @return
     */
    @GetMapping(value = "/keyUseTimeStatistics")
    public PageResp keyUseTimeStatistics() {
        return echartsServiceImpl.keyUseTimeStatistics();
    }

    /**
     * 钥匙故障次数
     * 子钥匙，车主钥匙，车主钥匙占比
     *
     * @return
     */
    @GetMapping(value = "/keyErrorTimeStatistics")
    public PageResp keyErrorTimeStatistics() {
        return echartsServiceImpl.keyErrorTimeStatistics();
    }

    /**
     * 按照时间段统计不同型号手机失败率、不同车型失败率、不同模块失败率
     * 不同手机型号的故障率,不同车型的故障率,不同故障码的故障率
     *
     * @return
     */
    @GetMapping(value = "/selectErrorStatusTotal")
    public PageResp selectErrorStatusTotal(@RequestBody TimeQuantumVO timeQuantumVO) {
        return echartsServiceImpl.selectErrorStatusTotal(timeQuantumVO.getStartTime(), timeQuantumVO.getEndTime());
    }


}