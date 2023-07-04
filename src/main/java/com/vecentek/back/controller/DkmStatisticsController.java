package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmFunctionalAbnormalServiceImpl;
import com.vecentek.back.service.impl.DkmStatisticsServiceImpl;
import com.vecentek.back.vo.TimeQuantumVO;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-10 14:59
 */

@RestController
@RequestMapping(value = "/dkmStatistics")
public class DkmStatisticsController {
    private static final String LOG_SERVICE = "http://dkserver-log/dkserver-log/dkmLog";

    /**
     * 查询最近的日志
     *
     * @return {@link PageResp}
     * @author EdgeYu
     * @date 2023-03-29 17:04
     */

    @Resource
    private DkmFunctionalAbnormalServiceImpl dkmFunctionalAbnormalServiceImpl;
    @Resource
    private DkmStatisticsServiceImpl echartsServiceImpl;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 查询所有业务id和业务
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023/4/10 15:03
     */
//    @GetMapping("/selectBusiness")
//    public PageResp selectBusiness() {
//
//        return dkmFunctionalAbnormalServiceImpl.selectBusiness();
//
//    }

//    @GetMapping("/selectRecentLogs")
//    public PageResp selectRecentLogs() {
//        return restTemplate.getForObject(UriComponentsBuilder.fromHttpUrl(LOG_SERVICE).path("/selectRecentLogs").build().toUriString(), PageResp.class);
//    }

    /**
     * 通过时间，userId，vin 分页查询日志
     *
     * @return {@link PageResp}
     * @author EdgeYu
     * @date 2023-03-29 17:05
     */
//    @GetMapping("/selectForPage")
//    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize, String startTime, String endTime, String userId, String vin,String businessId) {
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LOG_SERVICE).queryParam("pageIndex", pageIndex).queryParam("pageSize", pageSize).path("/selectForPage");
//        Optional.ofNullable(startTime).ifPresent(parameter -> builder.queryParam("startTime", parameter));
//        Optional.ofNullable(endTime).ifPresent(parameter -> builder.queryParam("endTime", parameter));
//        Optional.ofNullable(userId).ifPresent(parameter -> builder.queryParam("userId", parameter));
//        Optional.ofNullable(vin).ifPresent(parameter -> builder.queryParam("vin", parameter));
//        Optional.ofNullable(businessId).ifPresent(parameter -> builder.queryParam("businessId", parameter));
//        return restTemplate.getForObject(builder.build().toUriString(), PageResp.class);
//    }

//    @GetMapping("/selectFaultLogs")
//    public PageResp selectFaultLogs() {
//        return restTemplate.getForObject(UriComponentsBuilder.fromHttpUrl(LOG_SERVICE).path("/selectFaultLogs").build().toUriString(), PageResp.class);
//    }


    /**
     * 查询今日日志总数
     *
     * @return {@link PageResp}
     * @author EdgeYu
     * @date 2023-03-29 17:13
     */
//    @GetMapping("/selectTodayLogsCount")
//    public PageResp selectTodayLogsCount() {
//
//        return restTemplate.getForObject(UriComponentsBuilder.fromHttpUrl(LOG_SERVICE).path("/selectTodayLogsCount").build().toUriString(), PageResp.class);
//    }

//    @GetMapping("/selectPhoneBrandLogs")
//    public PageResp selectPhoneBrandLogs() {
//        return restTemplate.getForObject(UriComponentsBuilder.fromHttpUrl(LOG_SERVICE).path("/selectPhoneBrandLogs").build().toUriString(), PageResp.class);
//    }


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
     * @author EdgeYu
     * @date 2022-06-28 15:56
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