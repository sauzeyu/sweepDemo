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
import com.vecentek.common.response.PageResp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DkmSystemConfigurationExpiredServiceImplTest {

    @InjectMocks
    private DkmSystemConfigurationExpiredServiceImpl dkmSystemConfigurationExpiredService;

    @Mock
    private DkmKeyLogMapper dkmKeyLogMapper;

    @Mock
    private DkmSystemConfigurationExpiredMapper dkmSystemConfigurationExpiredMapper;

    @Mock
    private ProConfig proConfig;

    private DateTime dateTime;

    private DkmSystemConfigurationExpired dkmSystemConfigurationExpired;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(dkmSystemConfigurationExpiredService, "dkmKeyLogMapper", dkmKeyLogMapper);
        ReflectionTestUtils.setField(dkmSystemConfigurationExpiredService, "dkmSystemConfigurationExpiredMapper", dkmSystemConfigurationExpiredMapper);
        ReflectionTestUtils.setField(dkmSystemConfigurationExpiredService, "proConfig", proConfig);

        dateTime = DateUtil.parse("2022-12-30", "yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault());

        dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired();
        dkmSystemConfigurationExpired.setUsername("test");
        dkmSystemConfigurationExpired.setValidityPeriod(3);
        dkmSystemConfigurationExpired.setOperateTime(localDateTime);

        when(dkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        when(dkmSystemConfigurationExpiredMapper.insert(any(DkmSystemConfigurationExpired.class)))
                .thenReturn(1);

        when(dkmSystemConfigurationExpiredMapper.update(any(), any(LambdaUpdateWrapper.class)))
                .thenReturn(1);

        when(dkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(new ArrayList<>());

        when(proConfig.getSysDate()).thenReturn("2021-01-01");
    }

    @Test
    public void selectForExpirationTest() {
        PageResp result = dkmSystemConfigurationExpiredService.selectForExpiration();
        Assert.assertEquals(result.getData(), "没有过期钥匙日志待处理");

        DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setOperateTime(dateTime.toJdkDate());
        List<DkmKeyLog> list = new ArrayList<>();
        list.add(dkmKeyLog);

        when(dkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(list);

        result = dkmSystemConfigurationExpiredService.selectForExpiration();
        Assert.assertEquals(result.getData(), "2023-03-30前有过期钥匙日志待处理");
        TreeMap<String, Long> treeMap = new TreeMap<>();
        treeMap.put("2022-12=1", 1L);
        Assert.assertEquals(result.getExt(), treeMap);
    }

    @Test
    public void selectForLastTest() {
        PageResp result = dkmSystemConfigurationExpiredService.selectForLast();
        Assert.assertEquals(result.getData(), dkmSystemConfigurationExpired.getValidityPeriod());
    }

    @Test
    public void saveOrUpdateConfigExpiredTest1() {
        when(dkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        DkmSystemConfigurationExpired expired = new DkmSystemConfigurationExpired();
        expired.setUsername("test-1");
        expired.setValidityPeriod(5);

        PageResp result = dkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(expired);
        Assert.assertEquals(result.getCode(), 0);
        Assert.assertEquals(result.getData(), "新增配置成功");
    }

    @Test
    public void saveOrUpdateConfigExpiredTest2() {
        DkmSystemConfigurationExpired expired = new DkmSystemConfigurationExpired();
        expired.setUsername("test");
        expired.setValidityPeriod(5);

        PageResp result = dkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(expired);
        Assert.assertEquals(result.getCode(), 0);
        Assert.assertEquals(result.getData(), "修改配置成功");
    }

    @Test
    public void saveOrUpdateConfigExpiredTest3() {
        DkmSystemConfigurationExpired expired = new DkmSystemConfigurationExpired();
        expired.setUsername("test");
        expired.setValidityPeriod(5);

        when(dkmSystemConfigurationExpiredMapper.update(any(), any(LambdaUpdateWrapper.class)))
                .thenReturn(0);
        PageResp result = dkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(expired);
        Assert.assertEquals(result.getCode(), -1);
        Assert.assertEquals(result.getData(), "修改配置失败");
    }

    @Test
    public void deleteExpiredDkmKeyLogsTest() {
        LocalDate localDate = LocalDate.of(2022, 12, 30);

        when(dkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        when(proConfig.getSysDate()).thenReturn("2023-01-01");

        dkmSystemConfigurationExpiredService.deleteExpiredDkmKeyLogs();

        LambdaQueryWrapper<DkmKeyLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ge(DkmKeyLog::getOperateTime, LocalDate.of(2022, 1, 1))
                .le(DkmKeyLog::getOperateTime, localDate)
        ;
        List<DkmKeyLog> list = dkmKeyLogMapper.selectList(queryWrapper);

        Assert.assertTrue(list.isEmpty());
    }
}
