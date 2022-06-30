package com.vecentek.back.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Range;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liujz
 * @date 2022/6/30
 * @apiNote
 */
@Component
public class OTAStrategyShardingAlgorithm implements StandardShardingAlgorithm<String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    /**
     * 【范围】数据查询
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        // 逻辑表名
        String logicTableName = rangeShardingValue.getLogicTableName();

        // 范围参数
        Range<String> valueRange = rangeShardingValue.getValueRange();
        Set<String> queryRangeTables = extracted(logicTableName, LocalDateTime.parse(valueRange.lowerEndpoint(), formatter),
                LocalDateTime.parse(valueRange.upperEndpoint(), formatter));
        ArrayList<String> tables = new ArrayList<>(collection);
        tables.retainAll(queryRangeTables);
        System.out.println(JSON.toJSONString(tables));
        return tables;
    }

    /**
     * 根据范围计算表明
     *
     * @param logicTableName 逻辑表明
     * @param lowerEndpoint 范围起点
     * @param upperEndpoint 范围终端
     * @return 物理表名集合
     */
    private Set<String> extracted(String logicTableName, LocalDateTime lowerEndpoint, LocalDateTime upperEndpoint) {
        Set<String> rangeTable = new HashSet<>();
        while (lowerEndpoint.isBefore(upperEndpoint)) {
            String str = getTableNameByDate(lowerEndpoint, logicTableName);
            rangeTable.add(str);
            lowerEndpoint = lowerEndpoint.plusMonths(1);
        }
        // 获取物理表明
        String tableName = getTableNameByDate(upperEndpoint, logicTableName);
        rangeTable.add(tableName);
        return rangeTable;
    }

    /**
     * 根据日期获取表明
     * @param dateTime 日期
     * @param logicTableName 逻辑表名
     * @return 物理表名
     */
    private String getTableNameByDate(LocalDateTime dateTime, String logicTableName) {
        String tableSuffix = dateTime.format(yyyyMM);
        return logicTableName.concat("_").concat(tableSuffix);
    }

    /**
     * 数据插入
     *
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String str = preciseShardingValue.getValue();
        if (StringUtils.isEmpty(str)) {
            return collection.stream().findFirst().get();
        }
        LocalDateTime value = LocalDateTime.parse(str, formatter);
        String tableSuffix = value.format(yyyyMM);
        String logicTableName = preciseShardingValue.getLogicTableName();
        String table = logicTableName.concat("_").concat(tableSuffix);
        System.out.println("OrderStrategy.doSharding table name: " + table);
        return collection.stream().filter(s -> s.equals(table)).findFirst().orElseThrow(() -> new RuntimeException("逻辑分表不存在"));
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        //　自定义 这里需要spi支持
        return null;
    }

}
