package com.vecentek.back.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.constant.KeyErrorReasonEnum;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.dto.StatisticsDTO;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-10 15:44
 */
@Service
public class DkmStatisticsServiceImpl {

    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmKeyLogMapper dkmKeyLogMapper;

    public PageResp selectTotal(String startTime, String endTime) {
        int totalVehicles = dkmVehicleMapper.selectCount(Wrappers.<DkmVehicle>lambdaQuery()
                .ge(StrUtil.isNotBlank(startTime), DkmVehicle::getCreateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmVehicle::getCreateTime, endTime));
        int totalKeys = dkmKeyMapper.selectCount(Wrappers.<DkmKey>lambdaQuery()
                .ge(StrUtil.isNotBlank(startTime), DkmKey::getApplyTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKey::getApplyTime, endTime));
        int totalKeyError = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(StrUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 0));
        int totalKeyUse = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(StrUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 1));
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setKeyErrorCount(totalKeyError);
        statisticsDTO.setVehicleCount(totalVehicles);
        statisticsDTO.setKeyCount(totalKeys);
        statisticsDTO.setKeyUseCount(totalKeyUse);
        return PageResp.success("查询成功", statisticsDTO);
    }


    public PageResp selectKeyLogByMonth() {
        String[] monthList = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        List<MonthCountDTO> useLogCount = dkmKeyLogMapper.selectUseLogCountByMonth();
        List<MonthCountDTO> errorLogCount = dkmKeyLogMapper.selectErrorLogCountByMonth();
        List<Integer> useCountList = new ArrayList<>();
        List<Integer> errorCountList = new ArrayList<>();
        List<MonthCountDTO> emptyUseLogCountList = new ArrayList<>();
        List<MonthCountDTO> emptyErrorLogCountList = new ArrayList<>();

        for (String month : monthList) {
            MonthCountDTO useMonthCountDTO = new MonthCountDTO();
            MonthCountDTO errorMonthCountDTO = new MonthCountDTO();
            useMonthCountDTO.setMonth(month);
            errorMonthCountDTO.setMonth(month);
            useMonthCountDTO.setCount(0);
            errorMonthCountDTO.setCount(0);
            emptyUseLogCountList.add(useMonthCountDTO);
            emptyErrorLogCountList.add(errorMonthCountDTO);
        }
        for (MonthCountDTO monthCountDTO : useLogCount) {
            for (MonthCountDTO countDTO : emptyUseLogCountList) {
                if (monthCountDTO.getMonth().equals(countDTO.getMonth())) {
                    countDTO.setCount(monthCountDTO.getCount());
                }
            }
        }
        for (MonthCountDTO monthCountDTO : errorLogCount) {
            for (MonthCountDTO countDTO : emptyErrorLogCountList) {
                if (monthCountDTO.getMonth().equals(countDTO.getMonth())) {
                    countDTO.setCount(monthCountDTO.getCount());
                }
            }
        }
        emptyUseLogCountList.forEach(use -> useCountList.add(use.getCount()));

        emptyErrorLogCountList.forEach(error -> errorCountList.add(error.getCount()));

        // 今日使用次数
        int countUseToday = dkmKeyLogMapper.countUseToday();
        // 今日故障次数
        int countErrorToday = dkmKeyLogMapper.countErrorToday();
        JSONObject total = new JSONObject().set("useLogCount", useCountList)
                .set("errorLogCount", errorCountList)
                .set("countUseToday", countUseToday)
                .set("countErrorToday", countErrorToday);
        return PageResp.success("查询成功", total);
    }


    public PageResp selectKeyUseLogByTime(String startTime, String endTime) {
        List<CountDTO> keyLogCount = dkmKeyLogMapper.selectUseCountByTime(startTime, endTime);

        return PageResp.success("查询成功", keyLogCount);
    }

    public PageResp selectKeyErrorLogByTime(String startTime, String endTime) {
        List<CountDTO> keyLogCount = dkmKeyLogMapper.selectErrorCountByTime(startTime, endTime);
        return PageResp.success("查询成功", keyLogCount);
    }

    public PageResp selectKeyErrorLogByPhoneBrand(String phoneBrand) {
        List<CountDTO> list = dkmKeyLogMapper.selectKeyErrorLogByPhoneBrand(phoneBrand);
        for (CountDTO countDTO : list) {
            String name = countDTO.getName();
            String errorReason = KeyErrorReasonEnum.matchReason(name);
            countDTO.setName(errorReason);
        }
        return PageResp.success("查询成功",list);
    }

    /**
     * 车辆总数统计框
     * 按一年分成12个月查每个月的新增车辆
     * 每日新增车辆数
     * @return
     */
    public PageResp vehicleStatistics() {
        List<MonthCountDTO> vehicleMonthList = new ArrayList<>();
        int newToday = 0;
        // 每个月的车辆总数
        vehicleMonthList = dkmVehicleMapper.selectCountByMonth();
        // 每日新增车辆数
        newToday = dkmVehicleMapper.selectNewToday();
        JSONObject res = new JSONObject().set("vehicleMonthList", vehicleMonthList).set("newToday", newToday);
        return PageResp.success("查询成功",res);
    }

    /**
     * 钥匙总量统计框
     * 子钥匙，车主钥匙，车主钥匙占比
     * @return
     */
    public PageResp keyStatistics() {
        // 车主钥匙
        int masterCount = dkmKeyMapper.selectCount(new QueryWrapper<DkmKey>().lambda().eq(DkmKey::getParentId,"0"));
        // 子钥匙
        int childCount = dkmKeyMapper.selectCount(new QueryWrapper<DkmKey>().lambda().ne(DkmKey::getParentId,"0"));
        // 车主钥匙占比
        int num = masterCount;
        int allNum = masterCount + childCount;
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float)num/(float)allNum*100) + "%";
        JSONObject res = new JSONObject().set("masterCount", masterCount).set("childCount", childCount).set("proportion", result);
        return PageResp.success("查询成功",res);
    }

    public PageResp selectErrorStatusTotal(String startTime, String endTime) {
        if(StrUtil.isBlank(startTime) || StrUtil.isBlank(endTime)){
            return PageResp.fail(1001,"必填参数未传递或传入的参数格式不正确！");
        }
        HashMap<String, Object> phoneData = new HashMap<>();
        HashMap<String, Object> statusCodeData = new HashMap<>();
        HashMap<String, Object> vehicleData = new HashMap<>();
        // 手机品牌和操作码
        List<CountDTO> phoneList = dkmKeyLogMapper.selectPhoneErrorCountByTime(startTime,endTime);
        List<CountDTO> statusList = dkmKeyLogMapper.selectStatusErrorCountByTime(startTime,endTime);
        phoneData = count(phoneList);
        statusCodeData = count(statusList);
        // 车型 先查vin号再查车型
        List<CountDTO> vehicleList = dkmKeyLogMapper.selectVehicleErrorCountByTime(startTime,endTime);
        vehicleData = count(vehicleList);

        JSONObject res = new JSONObject().set("phoneData", phoneData).set("statusCodeData", statusCodeData).set("vehicleData", vehicleData);
        return PageResp.success("查询成功",res);
    }

    private HashMap<String, Object> count(List<CountDTO> list){
        HashMap<String, Object> map = new HashMap<>();
        if (list.size() > 0){
            int allNum = 0;
            for (CountDTO countDTO : list) {
                allNum += countDTO.getValue();
            }
            for (CountDTO countDTO : list) {
                int num = countDTO.getValue();
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(0);
                String result = numberFormat.format((float)num/(float)allNum*100) + "%";
                map.put(countDTO.getName(),result);
            }
        }
        return map;
    }
}
