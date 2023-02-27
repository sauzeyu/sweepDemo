package com.vecentek.back.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.config.ProConfig;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmSystemConfigurationExpired;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmSystemConfigurationExpiredMapper;
import com.vecentek.back.util.SpringContextUtil;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (DkmSystemConfigurationExpired)表服务实现类
 *
 * @author liujz
 * @since 2023-01-03 15:41:55
 */
@Service
@Slf4j
public class DkmSystemConfigurationExpiredServiceImpl {

    @Resource
    private DkmKeyLogMapper dkmKeyLogMapper;

    @Resource
    private DkmSystemConfigurationExpiredMapper dkmSystemConfigurationExpiredMapper;

    private static final ThreadFactory NAMED_FACTORY = new ThreadFactoryBuilder().setNameFormat("线程-删除key-%d").build();

    public PageResp selectForExpiration() {
        DateTime configInitTime = DateUtil.parse(SpringContextUtil.getBean(ProConfig.class).getSysDate(), "yyyy-MM-dd");
        // 获取系统配置的初始日期
        LocalDateTime configDateTime = LocalDateTime.ofInstant(configInitTime.toInstant(), ZoneId.systemDefault());
        LocalDate configDate = configDateTime.toLocalDate();
        LocalDate deadLine = getDeadLine();
        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.le(DkmKeyLog::getOperateTime, deadLine)
                .ge(DkmKeyLog::getOperateTime, configDate)
        ;
        List<DkmKeyLog> dkmKeyLogs = dkmKeyLogMapper.selectList(queryWrapper);
        int total = dkmKeyLogs.size();
        if (total > 0) {
            Map<String, Long> expiredLogs = dkmKeyLogs.stream()
                    .collect(Collectors.groupingBy(
                            dkmKeyLog -> YearMonth.from(dkmKeyLog.getOperateTime().toInstant().atZone(ZoneId.systemDefault())).toString(),
                            Collectors.counting()));
            return PageResp.success(deadLine + "前有过期钥匙日志待处理", (long) total, new TreeMap<>(expiredLogs));
        }

        return PageResp.success("没有过期钥匙日志待处理");
    }

    public PageResp selectForLast() {
        LambdaQueryWrapper<DkmSystemConfigurationExpired> wrapperExpired = Wrappers.<DkmSystemConfigurationExpired>lambdaQuery()
                .orderByDesc(DkmSystemConfigurationExpired::getOperateTime)
                .last("limit 1");
        DkmSystemConfigurationExpired dkmSystemConfigurationExpired = dkmSystemConfigurationExpiredMapper.selectOne(wrapperExpired);
        Integer validityPeriod = dkmSystemConfigurationExpired.getValidityPeriod();
        return PageResp.success("回显成功", validityPeriod);
    }

    public PageResp saveOrUpdateConfigExpired(DkmSystemConfigurationExpired dkmSystemConfigurationExpired) {
        // 从参数中获取用户名和有效期
        String username = dkmSystemConfigurationExpired.getUsername();
        Integer validityPeriod = dkmSystemConfigurationExpired.getValidityPeriod();

        // 创建查询Wrapper，用于在表中查找用户名与参数的用户名匹配的记录
        LambdaQueryWrapper<DkmSystemConfigurationExpired> wrapperExpired = Wrappers.<DkmSystemConfigurationExpired>lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(username), DkmSystemConfigurationExpired::getUsername, username);
        // 尝试在表中查找匹配的记录
        DkmSystemConfigurationExpired dkmSystemConfiguration = dkmSystemConfigurationExpiredMapper.selectOne(wrapperExpired);

        // 如果没有找到记录，将参数对象插入表中
        if (ObjectUtils.isEmpty(dkmSystemConfiguration)) {
            dkmSystemConfigurationExpiredMapper.insert(dkmSystemConfigurationExpired);
            return PageResp.success("新增配置成功");
        }

        // 如果找到了记录，创建更新Wrapper，用于更新记录的有效期
        LambdaUpdateWrapper<DkmSystemConfigurationExpired> updateWrapper = Wrappers.<DkmSystemConfigurationExpired>lambdaUpdate()
                .set(DkmSystemConfigurationExpired::getValidityPeriod, validityPeriod)
                .eq(ObjectUtils.isNotEmpty(username), DkmSystemConfigurationExpired::getUsername, username);

        // 尝试更新表中的记录
        int update = dkmSystemConfigurationExpiredMapper.update(null, updateWrapper);

        // 返回更新是否成功的消息
        if (update == 0) {
            return PageResp.fail("修改配置失败");
        }
        return PageResp.success("修改配置成功");
    }

    {

        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1, NAMED_FACTORY);


        Runnable task = () -> {

            LocalDate deadLine = getDeadLine();
            LocalDate nowDate = LocalDate.now();
            boolean deleteLogs = false;
            if (nowDate.isEqual(deadLine)) {
                log.info(nowDate + " 和 " + deadLine + " 是同一天");
                deleteLogs = true;

            } else {
                log.info(nowDate + " 和 " + deadLine + " 不是同一天");
            }
            DateTime configInitTime = DateUtil.parse(SpringContextUtil.getBean(ProConfig.class).getSysDate(), "yyyy-MM-dd");
            if (deleteLogs) {
                // 执行过期钥匙使用记录删除操作
                dkmKeyLogMapper.delete(Wrappers.<DkmKeyLog>lambdaQuery()
                        .ge(DkmKeyLog::getOperateTime, configInitTime)
                        .le(DkmKeyLog::getOperateTime, deadLine)
                );
            }
        };
        //安排在指定的时间执行指定的任务。task 将在每天的3点执行一次
        scheduledExecutor.scheduleAtFixedRate(task, getSpecifiedTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), TimeUnit.MILLISECONDS);
    }

    public long getSpecifiedTime() {
        // 设置为指定的年月日时分秒 此处方法内易知 参数含义 不属于魔法数字
        LocalDateTime dateTime = LocalDateTime.of(2022, Month.JANUARY, 1, 3, 0, 0);
        Instant instant = Instant.ofEpochMilli(dateTime.toEpochSecond(ZoneOffset.ofHours(8)) * 1000);
        return instant.toEpochMilli();
    }


    public LocalDate getDeadLine() {
        // 获取系统配置过期表的最近配置的数据
        LambdaQueryWrapper<DkmSystemConfigurationExpired> wrapperExpired = Wrappers.<DkmSystemConfigurationExpired>lambdaQuery()
                .orderByDesc(DkmSystemConfigurationExpired::getOperateTime)
                .last("limit 1");
        DkmSystemConfigurationExpired dkmSystemConfigurationExpired = dkmSystemConfigurationExpiredMapper.selectOne(wrapperExpired);
        Integer validityPeriod = dkmSystemConfigurationExpired.getValidityPeriod();

        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 根据系统配置过期表最新配置 钥匙使用记录保存时间（月） 和当前时间 计算过期时间
        Period period = Period.ofMonths(-validityPeriod);
        return now.plus(period);
    }
}

