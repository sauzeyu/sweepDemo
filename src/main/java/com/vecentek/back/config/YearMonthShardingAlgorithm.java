package com.vecentek.back.config;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.collect.Range;
import com.vecentek.back.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 根据年月分表规则
 *
 * @author ：liubo
 * @version ：1.0
 * @since 2022-02-10 14:59
 */
@Component
@Slf4j
public class YearMonthShardingAlgorithm implements StandardShardingAlgorithm {


    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final Map<Class<?>, Function<Object, String>> FORMATTER_MAP = new HashMap<>();

    static {
        FORMATTER_MAP.put(String.class, Object::toString);
        FORMATTER_MAP.put(Timestamp.class, date -> new SimpleDateFormat(DATE_FORMAT).format((Timestamp) date));
        FORMATTER_MAP.put(LocalDate.class, date -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDateTime localDateTime = LocalDateTime.of((LocalDate) date, LocalTime.MIN);
            return formatter.format(localDateTime);
        });
    }


    public Date getConfigDate() {
        ProConfig proConfig = SpringContextUtil.getBean(ProConfig.class);
        if (CharSequenceUtil.isEmpty(proConfig.getSysDate())) {
            throw new IllegalArgumentException("未设置系统初始时间");
        }
        return DateUtil.parse(proConfig.getSysDate(), "yyyy-MM-dd");
    }

    /**
     * 获取分表时间集
     *
     * @return Integer
     */
    public Integer getSubTable() {
        TestProperties bean = SpringContextUtil.getBean(TestProperties.class);
        return bean.getFlag();
    }

    @Override
    public void init() {
        // 在这里进行分片算法的初始化操作，例如读取配置文件、创建数据结构等
    }

    @Override
    public String getType() {
        return null;
    }

    /**
     * 插入策略
     */
    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        //获取字段值
        Date time;
        if (preciseShardingValue.getValue() instanceof String) {
            time = DateUtil.parse(preciseShardingValue.getValue().toString());
        } else {
            time = (Date) preciseShardingValue.getValue();
        }
        String year = DateUtil.date(time).toString("yyyyMM");

        int table = Integer.parseInt(year.substring(year.length() - 2));

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


    @Override
    public Collection<String> doSharding(Collection availableTargetNames, RangeShardingValue rangeShardingValue) {
        // 方法实现没法指定泛型
        Range<?> valueRange = rangeShardingValue.getValueRange();

        String lowerDate;
        String upperDate;
        if (!FORMATTER_MAP.containsKey(valueRange.lowerEndpoint().getClass())) {
            throw new IllegalArgumentException("不支持的时间类型");
        }
        lowerDate = FORMATTER_MAP.get(valueRange.lowerEndpoint().getClass()).apply(valueRange.lowerEndpoint());
        upperDate = FORMATTER_MAP.get(valueRange.upperEndpoint().getClass()).apply(valueRange.upperEndpoint());

        // 获取数据库节点名称
        List<String> tableNames = getTableNames(lowerDate, upperDate, rangeShardingValue.getLogicTableName());
        if (tableNames.isEmpty()) {
            throw new IllegalArgumentException("没有对应的数据表");
        }


        return new ArrayList<>(tableNames);
    }


    /**
     * 获取符合时间段的数据库节点名称列表
     */
    private List<String> getTableNames(String lowerDate, String upperDate, String logicTableName) {
        List<String> tableNames = new ArrayList<>();
        //系统时间
        Date sysStartTime = getConfigDate();
        DateTime sst = DateUtil.parseDateTime(lowerDate);
        if (sysStartTime.compareTo(sst) > 0) {
            lowerDate = DateUtil.format(sysStartTime, "yyyy-MM-dd 00:00:00");
        }
        //获取时间段内的所有年月，不会超过当前时间
        List<String> suffixList = getSuffixListForRangeByMonth(lowerDate, upperDate);
        suffixList.forEach(prefix -> tableNames.add(logicTableName + "_" + prefix));
        return tableNames;
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
        List<String> suffixList = collect.stream().distinct().collect(Collectors.toList());

        List<String> list = suffixList.stream().sorted(Comparator.comparing(Integer::parseInt)).collect(Collectors.toList());
        Integer subTable = getSubTable();
        String lastStr = list.get(list.size() - 1);
        int lastNum = Integer.parseInt(lastStr.substring(lastStr.length() - 2));
        if (lastNum % subTable != 0) {
            lastNum = lastNum + subTable - (lastNum % subTable);
            String newLastNum = String.format("%2d", lastNum).replace(" ", "0");
            list.set(list.size() - 1, lastStr.substring(0, 4) + newLastNum);
        }


        return list.stream().filter(time -> Integer.parseInt(time.substring(time.length() - 2)) % subTable == 0).collect(Collectors.toList());
    }

}
