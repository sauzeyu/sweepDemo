package com.vecentek.back.service.impl;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.config.ProConfig;
import com.vecentek.back.config.YearMonthShardingAlgorithm;
import com.vecentek.back.constant.KeyErrorReasonEnum;
import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.dto.StatisticsDTO;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.util.DownLoadUtil;
import com.vecentek.back.util.SpringContextUtil;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    public PageResp selectTotal(Date startTime, Date endTime) {
        if (ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)) {
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        int totalVehicles = dkmVehicleMapper.selectCount(Wrappers.<DkmVehicle>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmVehicle::getCreateTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmVehicle::getCreateTime, endTime));
        int totalKeys = dkmKeyMapper.selectCount(Wrappers.<DkmKey>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKey::getApplyTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKey::getApplyTime, endTime));
        int totalKeyError = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 0));
        int totalKeyUse = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 1));
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setKeyErrorCount(totalKeyError);
        statisticsDTO.setVehicleCount(totalVehicles);
        statisticsDTO.setKeyCount(totalKeys);
        statisticsDTO.setKeyUseCount(totalKeyUse);
        return PageResp.success("查询成功", statisticsDTO);
    }

    public Date[] getTime() {
        ProConfig proConfig = SpringContextUtil.getBean(ProConfig.class);
        Date startTime = DateUtil.parse(proConfig.getSysDate(), "yyyy-MM-dd");
        Date endTime = new Date();
        return new Date[]{startTime,endTime};
    }
    public PageResp selectVehicleAndKeyAndKeyLogTotal() {
        // 获取分表字段的开始日期与结束日期
        Date[] time = getTime();
        Date startTime = time[0];
        Date endTime = time[1];
        int totalVehicles = dkmVehicleMapper.selectCount(null);
        int totalKeys = dkmKeyMapper.selectCount(Wrappers.<DkmKey>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKey::getApplyTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKey::getApplyTime, endTime));

        int totalKeyError = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 0));
        int totalKeyUse = dkmKeyLogMapper.selectCount(Wrappers.<DkmKeyLog>lambdaQuery()
                .ge(ObjectUtil.isNotNull(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKeyLog::getOperateTime, endTime)
                .eq(DkmKeyLog::getFlag, 1));

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setKeyErrorCount(totalKeyError);
        statisticsDTO.setVehicleCount(totalVehicles);
        statisticsDTO.setKeyCount(totalKeys);
        statisticsDTO.setKeyUseCount(totalKeyUse);
        return PageResp.success("查询成功", statisticsDTO);
    }


    public PageResp selectKeyLogByMonth() {
        List<String> monthList = MonthCountDTO.generateMonthList();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date nowYear = c.getTime();
        String endTime = format.format(nowYear);
        c.add(Calendar.YEAR, -1);
        Date lastYear =  c.getTime();
        String startTime = format.format(lastYear);


        List<MonthCountDTO> useLogCount = dkmKeyLogMapper.selectUseLogCountByMonth(startTime, endTime);

        useLogCount = MonthCountDTO.checkMonthCount(useLogCount, monthList);

        List<Integer> useLogCountList = MonthCountDTO.countToList(useLogCount);

        List<MonthCountDTO> errorLogCount = dkmKeyLogMapper.selectErrorLogCountByMonth(startTime, endTime);
        errorLogCount = MonthCountDTO.checkMonthCount(errorLogCount, monthList);
        List<Integer> errorLogCountList = MonthCountDTO.countToList(errorLogCount);

        JSONObject total = new JSONObject()
                .set("monthList", monthList)
                .set("useLogCount", useLogCountList)
                .set("errorLogCount", errorLogCountList);
        return PageResp.success("查询成功", total);
    }


    // TODO List重复代码抽取常量
    public PageResp selectKeyUseLogByTime(String startTime, String endTime) {
        List<CountDTO> keyLogCount = dkmKeyLogMapper.selectUseCountByTime(startTime, endTime);
        // 数据简化处理 四位状态码的日志只返回开启和关闭两大类 不显示具体开启情况
        List<CountDTO> list1 = simpleLog(keyLogCount, "01");
        List<CountDTO> list2 = simpleLog(keyLogCount, "03");
        List<CountDTO> list3 = simpleLog(keyLogCount, "04");
        List<CountDTO> list4 = simpleLog(keyLogCount, "05");
        List<CountDTO> list5 = simpleLog(keyLogCount, "06");
        List<CountDTO> list6 = simpleLog(keyLogCount, "07");
        List<CountDTO> list7 = simpleLog(keyLogCount, "08");
        List<CountDTO> list8 = simpleLog(keyLogCount, "09");
        List<CountDTO> list9 = simpleLog(keyLogCount, "0A");
        List<CountDTO> list10 = simpleLog2(keyLogCount, "0C");
        // 两位状态码的日志 不做简化处理
        List<CountDTO> list11 = twoBytesLog(keyLogCount);
        list1.addAll(list2);
        list1.addAll(list3);
        list1.addAll(list4);
        list1.addAll(list5);
        list1.addAll(list6);
        list1.addAll(list7);
        list1.addAll(list8);
        list1.addAll(list9);
        list1.addAll(list10);
        list1.addAll(list11);
        return PageResp.success("查询成功", list1);
    }

    public PageResp selectKeyErrorLogByTime(String startTime, String endTime) {
        List<CountDTO> keyLogCount = dkmKeyLogMapper.selectErrorCountByTime(startTime, endTime);
        // 数据简化处理 四位状态码的日志只返回开启和关闭两大类 不显示具体开启情况
        List<CountDTO> list1 = simpleLog(keyLogCount, "01");
        List<CountDTO> list2 = simpleLog(keyLogCount, "03");
        List<CountDTO> list3 = simpleLog(keyLogCount, "04");
        List<CountDTO> list4 = simpleLog(keyLogCount, "05");
        List<CountDTO> list5 = simpleLog(keyLogCount, "06");
        List<CountDTO> list6 = simpleLog(keyLogCount, "07");
        List<CountDTO> list7 = simpleLog(keyLogCount, "08");
        List<CountDTO> list8 = simpleLog(keyLogCount, "09");
        List<CountDTO> list9 = simpleLog(keyLogCount, "0A");
        List<CountDTO> list10 = simpleLog2(keyLogCount, "0C");
        // 两位状态码的日志 不做简化处理
        List<CountDTO> list11 = twoBytesLog(keyLogCount);
        list1.addAll(list2);
        list1.addAll(list3);
        list1.addAll(list4);
        list1.addAll(list5);
        list1.addAll(list6);
        list1.addAll(list7);
        list1.addAll(list8);
        list1.addAll(list9);
        list1.addAll(list10);
        list1.addAll(list11);
        return PageResp.success("查询成功", list1);
    }

    public PageResp selectKeyErrorLogByPhoneBrand(String phoneBrand) {
        List<CountDTO> list;
        if (StrUtil.isBlank(phoneBrand)) {
            list = dkmKeyLogMapper.selectKeyErrorLog();
        } else {
            list = dkmKeyLogMapper.selectKeyErrorLogByPhoneBrand(phoneBrand);

        }
        for (CountDTO countDTO : list) {
            String name = countDTO.getName();
            String reason = KeyErrorReasonEnum.matchReason(name);
            countDTO.setName(reason);
        }
        return PageResp.success("查询成功", list);
    }

    /**
     * 车辆总数统计框
     * 按一年分成12个月查每个月的新增车辆
     * 每日新增车辆数
     *
     * @return
     */
    public PageResp vehicleStatistics() {
        List<MonthCountDTO> vehicleMonthList;

        int newToday;
        // 每个月的车辆总数
        vehicleMonthList = MonthCountDTO.checkMonthCount(dkmVehicleMapper.selectCountByMonth());
        List<Integer> countList = MonthCountDTO.countToList(vehicleMonthList);

        newToday = dkmVehicleMapper.selectNewToday();
        JSONObject res = new JSONObject().set("vehicleMonthList", countList).set("newToday", newToday);
        return PageResp.success("查询成功", res);
    }

    /**
     * 钥匙总量统计框
     * 子钥匙，车主钥匙，车主钥匙占比
     *
     * @return
     */
    public PageResp keyStatistics() {
        Date[] time = getTime();
        Date startTime = time[0];
        Date endTime = time[1];
        // 车主钥匙
        int masterCount = dkmKeyMapper.selectCount(
                new QueryWrapper<DkmKey>().lambda()
                        .ge(ObjectUtil.isNotNull(startTime), DkmKey::getApplyTime, startTime)
                        .le(ObjectUtil.isNotNull(endTime), DkmKey::getApplyTime, endTime)
                        .eq(DkmKey::getParentId, "0"));

        // 子钥匙
        int childCount = dkmKeyMapper.selectCount(new QueryWrapper<DkmKey>().lambda()
                .ge(ObjectUtil.isNotNull(startTime), DkmKey::getApplyTime, startTime)
                .le(ObjectUtil.isNotNull(endTime), DkmKey::getApplyTime, endTime)
                .ne(DkmKey::getParentId, "0"));
        // 车主钥匙占比

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) masterCount / (float) (masterCount + childCount) * 100) + "%";


        JSONObject res = new JSONObject().set("masterCount", masterCount).set("childCount", childCount).set("proportion", result);
        return PageResp.success("查询成功", res);
    }

    public PageResp selectErrorStatusTotal(Date startTime, Date endTime) {
        if (ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)) {
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        HashMap<String, Object> phoneData;
        HashMap<String, Object> statusCodeData;
        HashMap<String, Object> vehicleData;
        // 手机品牌和操作码
        List<CountDTO> phoneList = dkmKeyLogMapper.selectPhoneErrorCountByTime(startTime, endTime);
        List<CountDTO> statusList = dkmKeyLogMapper.selectStatusErrorCountByTime(startTime, endTime);
        phoneData = count(phoneList);
        statusCodeData = count(statusList);
        // 车型 先查vin号再查车型
        List<CountDTO> vehicleList = dkmKeyLogMapper.selectVehicleErrorCountByTime(startTime, endTime);
        vehicleData = count(vehicleList);

        JSONObject res = new JSONObject().set("phoneData", phoneData).set("statusCodeData", statusCodeData).set("vehicleData", vehicleData);
        return PageResp.success("查询成功", res);
    }

    // TODO 注释
    private HashMap<String, Object> count(List<CountDTO> list) {
        HashMap<String, Object> map = new HashMap<>();
        if (list.size() > 0) {
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
                String result = numberFormat.format((float) num / (float) allNum * 100) + "%";
                map.put(countDTO.getName(), result);
            }
        }
        return map;
    }

    public PageResp keyUseTimeStatistics() {


        String now = DownLoadUtil.getNow();
        String lastDay = DownLoadUtil.getLastDay();
        String yearFirstDay = DownLoadUtil.getCurrYearFirst();
        String yearLastDay = DownLoadUtil.getCurrYearLast();
        // 今日使用次数
        int countUseToday = dkmKeyLogMapper.countUseToday(now,lastDay);
        // 每个月的使用数
        List<MonthCountDTO> useMonthList = MonthCountDTO.checkMonthCount(dkmVehicleMapper.countUseByMonth(yearFirstDay,yearLastDay));
        List<Integer> countList = MonthCountDTO.countToList(useMonthList);
        JSONObject res = new JSONObject().set("countUseToday", countUseToday).set("useMonthList", countList);
        return PageResp.success("查询成功", res);
    }

    public PageResp keyErrorTimeStatistics() {
        String now = DownLoadUtil.getNow();
        String lastDay = DownLoadUtil.getLastDay();
        String yearFirstDay = DownLoadUtil.getCurrYearFirst();
        String yearLastDay = DownLoadUtil.getCurrYearLast();
        // 今日故障次数
        int countErrorToday = dkmKeyLogMapper.countErrorToday(now,lastDay);
        // 每个月的使用数
        List<MonthCountDTO> errorMonthList = MonthCountDTO.checkMonthCount(dkmVehicleMapper.countErrorByMonth(yearFirstDay,yearLastDay));
        List<Integer> countList = MonthCountDTO.countToList(errorMonthList);
        JSONObject res = new JSONObject().set("countErrorToday", countErrorToday).set("errorMonthList", countList);
        return PageResp.success("查询成功", res);
    }

    public PageResp selectKeyErrorLogByAllPhoneBrand() {
        Date[] time = getTime();
        Date startTime = time[0];
        Date endTime = time[1];
        List<CountDTO> list = dkmKeyLogMapper.selectKeyErrorLogByAllPhoneBrand(startTime,endTime);
        return PageResp.success("查询成功", list);
    }

    /**
     * 简化数据
     *
     * @param countDTOs
     * @param statusCode
     * @return
     */
    List<CountDTO> simpleLog(List<CountDTO> countDTOs, String statusCode) {
        String type = "";
        List<CountDTO> data = new ArrayList<>();
        int OpenInt = 0; // 开启总条数

        for (CountDTO countDTO : countDTOs) {
            if (countDTO.getName().length() == 4) {
                if (ObjectUtil.equals(statusCode, "01")) {
                    type = "解闭锁";
                } else if (ObjectUtil.equals(statusCode, "03")) {
                    type = "寻车";
                } else if (ObjectUtil.equals(statusCode, "04")) {
                    type = "发动机";
                } else if (ObjectUtil.equals(statusCode, "05")) {
                    type = "空调";
                } else if (ObjectUtil.equals(statusCode, "06")) {
                    type = "司机车窗";
                } else if (ObjectUtil.equals(statusCode, "07")) {
                    type = "副驾车窗";
                } else if (ObjectUtil.equals(statusCode, "08")) {
                    type = "左后车窗";
                } else if (ObjectUtil.equals(statusCode, "09")) {
                    type = "右后车窗";
                } else if (ObjectUtil.equals(statusCode, "0A")) {
                    type = "遮阳罩";
                }

                if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), statusCode)
                        && ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 2, 4), "00")) { // 关闭
                    countDTO.setName(type + "关闭");
                    countDTO.setValue(countDTO.getValue());
                    data.add(countDTO);
                } else if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), statusCode)
                        && ObjectUtil.notEqual(StrUtil.sub(countDTO.getName(), 2, 4), "00")) { // 开启
                    Integer value = countDTO.getValue();
                    int intValue = value.intValue();
                    OpenInt += intValue;
                }
            }
        }

        if (OpenInt != 0) {
            CountDTO countDTO = new CountDTO();
            countDTO.setName(type + "开启");
            countDTO.setValue(OpenInt);
            data.add(countDTO);
        }
        return data;
    }

    /**
     * 主驾副驾通风加热
     *
     * @param countDTOs
     * @param statusCode
     * @return
     */
    List<CountDTO> simpleLog2(List<CountDTO> countDTOs, String statusCode) {
        String type = "通风加热";
        List<CountDTO> data = new ArrayList<>();
        int mainOpenInt = 0; // 主驾通风加热开启总条数
        int secOpenInt = 0; // 副驾通风加热开启总条数

        for (CountDTO countDTO : countDTOs) {
            if (countDTO.getName().length() == 4) {
                // 主驾通风加热特殊处理
                if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), "0C")
                        && ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 2, 4), "00")) { // 主驾通风加热关闭
                    countDTO.setName("主驾" + type + "关闭");
                    countDTO.setValue(countDTO.getValue());
                    data.add(countDTO);
                } else if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), "0C")
                        && Convert.toInt(StrUtil.sub(countDTO.getName(), 2, 4)) <= 12) { // 主驾通风加热开启
                    Integer value = countDTO.getValue();
                    int intValue = value.intValue();
                    mainOpenInt += intValue;
                }

                // 副驾通风加热特殊处理
                if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), "0C")
                        && ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 2, 4), "13")) { // 主驾通风加热关闭
                    countDTO.setName("副驾" + type + "关闭");
                    countDTO.setValue(countDTO.getValue());
                    data.add(countDTO);
                } else if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), "0C")
                        && Convert.toInt(StrUtil.sub(countDTO.getName(), 2, 4)) >= 14
                        && Convert.toInt(StrUtil.sub(countDTO.getName(), 2, 4)) <= 25) { // 主驾通风加热开启
                    Integer value = countDTO.getValue();
                    int intValue = value.intValue();
                    secOpenInt += intValue;
                }

            }
        }
        if (ObjectUtil.equals(statusCode, "0C") && mainOpenInt != 0) {
            CountDTO countDTO = new CountDTO();
            countDTO.setName("主驾" + type + "开启");
            countDTO.setValue(mainOpenInt);
            data.add(countDTO);
        }

        if (ObjectUtil.equals(statusCode, "0C") && secOpenInt != 0) {
            CountDTO countDTO = new CountDTO();
            countDTO.setName("副驾" + type + "开启");
            countDTO.setValue(secOpenInt);
            data.add(countDTO);
        }
        return data;
    }

    /**
     * 两位状态码的日志
     *
     * @param countDTOs
     * @return
     */
    List<CountDTO> twoBytesLog(List<CountDTO> countDTOs) {
        List<CountDTO> data = new ArrayList<>();
        for (CountDTO countDTO : countDTOs) {
            if (countDTO.getName().length() == 2) {
                String matchName = KeyStatusCodeEnum.matchName(countDTO.getName());
                countDTO.setName(matchName);
                data.add(countDTO);
            }
        }
        return data;
    }
}
