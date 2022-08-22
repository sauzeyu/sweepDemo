package com.vecentek.back.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Range;
import com.vecentek.back.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据年月分表规则
 */
@Component
@Slf4j
public class YearMonthShardingAlgorithm implements StandardShardingAlgorithm {

    //获取系统初始时间，确定安时间分表后的最小表名
    public Date getSysDate() {
        ProConfig proConfig = SpringContextUtil.getBean(ProConfig.class);
        if (StrUtil.isEmpty(proConfig.getSysDate())) {
            throw new RuntimeException("未设置系统初始时间");
        }
        return DateUtil.parse(proConfig.getSysDate(), "yyyy-MM-dd");
    }

    //获取分表时间集
    public Integer getSubTable() {
        TestProperties bean = SpringContextUtil.getBean(TestProperties.class);
        return bean.getFlag();
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return null;
    }

    //插入策略
    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        //获取字段值
        Date time = null;
        if (preciseShardingValue.getValue() instanceof String) {
            time = DateUtil.parse(preciseShardingValue.getValue().toString());
        } else {
            time = (Date) preciseShardingValue.getValue();
        }
        String year = DateUtil.date(time).toString("yyyyMM");

        Integer table = Integer.parseInt(year.substring(year.length() - 2));

        Integer subTable = getSubTable();
        String logicTableName = preciseShardingValue.getLogicTableName();
        String tableName = logicTableName + "_" + year;
        if (table >= subTable) {
            table = table % subTable == 0 ? table : table + (table % subTable);
            String newTable = String.format("%2d", table).replace(" ", "0");
            year = DateUtil.date(time).toString("yyyy" + newTable);
            return logicTableName + "_" + year;
        }

        return tableName;
    }


    //处理范围查询
    @Override
    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
        //返回数据库节点名称list
        Collection<String> collect = new ArrayList<>();
        Range valueRange = rangeShardingValue.getValueRange();
        //获取查询条件中范围值
        //Range<String> valueRange = rangeShardingValue.getValueRange();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (valueRange.lowerEndpoint() instanceof String) {

            String lowerDate = (String) valueRange.lowerEndpoint();
            //系统时间
            Date sysStartTime = getSysDate();
            DateTime sst = DateUtil.parseDateTime(lowerDate);
            if (sysStartTime.compareTo(sst) > 0) {
                lowerDate = DateUtil.format(sysStartTime, "yyyy-MM-dd 00:00:00");
            }
            String upperDate = (String) valueRange.upperEndpoint();
            //获取时间段内的所有年月，不会超过当前时间
            List<String> suffixList = getSuffixListForRangeByMonth(lowerDate, upperDate);
            suffixList.forEach(yyyyMM -> {
                collect.add(rangeShardingValue.getLogicTableName() + "_" + yyyyMM);
            });
            if (collect.size() == 0) {
                throw new RuntimeException("没有对应的数据表");
            }
        }
        if (valueRange.lowerEndpoint() instanceof Timestamp) {
            Timestamp lowerTime = (Timestamp) valueRange.lowerEndpoint();
            String lowerDate = df.format(lowerTime);
            //系统时间
            Date sysStartTime = getSysDate();
            DateTime sst = DateUtil.parseDateTime(lowerDate);
            if (sysStartTime.compareTo(sst) > 0) {
                lowerDate = DateUtil.format(sysStartTime, "yyyy-MM-dd 00:00:00");
            }
            Timestamp upperTime = (Timestamp) valueRange.upperEndpoint();
            String upperDate = df.format(upperTime);
            //获取时间段内的所有年月，不会超过当前时间
            List<String> suffixList = getSuffixListForRangeByMonth(lowerDate, upperDate);
            suffixList.forEach(yyyyMM -> {
                collect.add(rangeShardingValue.getLogicTableName() + "_" + yyyyMM);
            });
            if (collect.size() == 0) {
                throw new RuntimeException("没有对应的数据表");
            }
        }
        return collect;
    }

    /**
     * 两个日期之间所有月份
     */
    public List<String> getSuffixListForRangeByMonth(String lowerDate, String upperDate) {
        DateTime st = DateUtil.parseDateTime(lowerDate);
        DateTime et = DateUtil.parseDateTime(upperDate);
        DateTime ct = DateUtil.parseDateTime(DateUtil.format(new Date(), "yyyy-MM-dd 23:59:59"));
        //判断结束时间不能大于当前时间
        if (et.compareTo(ct) > 0) {
            et = ct;
        }
        List<DateTime> dateTimes = DateUtil.rangeToList(st, et, DateField.DAY_OF_MONTH);
        List<String> collect = dateTimes.stream().map(x -> x.toString("yyyyMM")).collect(Collectors.toList());
        HashSet h = new HashSet(collect);
        List<String> suffixList = ListUtil.toList(h);

        List<String> list = suffixList.stream().sorted(Comparator.comparing(Integer::parseInt)).collect(Collectors.toList());
        Integer subTable = getSubTable();
        String lastStr = list.get(list.size() - 1);
        int lastNum = Integer.parseInt(lastStr.substring(lastStr.length() - 2));
        if (lastNum % subTable != 0) {
            lastNum = lastNum + subTable - (lastNum % subTable);
            String newLastNum = String.format("%2d", lastNum).replace(" ", "0");
            list.set(list.size() - 1, lastStr.substring(0, 4) + newLastNum);
        }

        List<String> subTableList = list.stream()
                .filter(time -> Integer.parseInt(time.substring(time.length() - 2)) % subTable == 0).collect(Collectors.toList());
        return subTableList;
    }

}
