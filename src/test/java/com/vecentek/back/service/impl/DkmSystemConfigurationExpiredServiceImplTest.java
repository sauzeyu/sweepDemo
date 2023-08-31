package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.config.ProConfig;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmSystemConfigurationExpired;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmSystemConfigurationExpiredMapper;
import com.vecentek.back.util.SpringContextUtil;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmSystemConfigurationExpiredServiceImplTest {

    @Mock
    private DkmKeyLogMapper mockDkmKeyLogMapper;
    @Mock
    private DkmSystemConfigurationExpiredMapper mockDkmSystemConfigurationExpiredMapper;

    @InjectMocks
    private DkmSystemConfigurationExpiredServiceImpl dkmSystemConfigurationExpiredServiceImplUnderTest;
    private MockedStatic springContextUtilMocked;
    private ProConfig proConfig = new ProConfig();
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLog.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmSystemConfigurationExpired.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKey.class);
        MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        springContextUtilMocked = springContextUtilMockedStatic;

        proConfig.setSysDate("2021-01-01");
    }


    @AfterEach
    public void afterEach() {
        springContextUtilMocked.close();
    }
    @Test
    void testSelectForExpiration() {
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        // Setup
        final PageResp expectedResult = PageResp.success("没有过期钥匙日志待处理");

        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        // Configure DkmKeyLogMapper.selectList(...).
        final DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setId(0L);
        dkmKeyLog.setVin("vin");
        dkmKeyLog.setKeyId("keyId");
        dkmKeyLog.setPhoneModel("phoneModel");
        dkmKeyLog.setUserId("userId");
        dkmKeyLog.setPhoneBrand("phoneBrand");
        dkmKeyLog.setFlag(0);
        dkmKeyLog.setFlagVO("flagVO");
        dkmKeyLog.setStatusCode("statusCode");
        dkmKeyLog.setErrorReason("errorReason");
        dkmKeyLog.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        dkmKeyLog.setCreator("creator");
        dkmKeyLog.setUpdator("updator");
        dkmKeyLog.setCreateTime("createTime");
        dkmKeyLog.setUpdateTime("updateTime");
        final List<DkmKeyLog> dkmKeyLogs = Arrays.asList(dkmKeyLog);
        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeyLogs);

        // Run the test
        final PageResp result = dkmSystemConfigurationExpiredServiceImplUnderTest.selectForExpiration();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForExpiration_DkmKeyLogMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("没有过期钥匙日志待处理");

        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmSystemConfigurationExpiredServiceImplUnderTest.selectForExpiration();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForLast() {
        // Setup
        final PageResp expectedResult = PageResp.success("回显成功");

        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        // Run the test
        final PageResp result = dkmSystemConfigurationExpiredServiceImplUnderTest.selectForLast();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSaveOrUpdateConfigExpired() {
        // Setup
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final PageResp expectedResult = PageResp.success("修改配置成功");

        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired1 = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired1);

        when(mockDkmSystemConfigurationExpiredMapper.insert(new DkmSystemConfigurationExpired(0, "username", 0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);
        when(mockDkmSystemConfigurationExpiredMapper.update(any(), any()))
                .thenReturn(1);

        // Run the test
        final PageResp result = dkmSystemConfigurationExpiredServiceImplUnderTest.saveOrUpdateConfigExpired(
                dkmSystemConfigurationExpired);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmSystemConfigurationExpiredMapper).insert(new DkmSystemConfigurationExpired(0, "username", 0,
        //        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testDeleteExpiredDkmKeyLogs() {
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        // Setup
        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        when(mockDkmKeyLogMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Run the test
        dkmSystemConfigurationExpiredServiceImplUnderTest.deleteExpiredDkmKeyLogs();

        // Verify the results
        //verify(mockDkmKeyLogMapper).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetDeadLine() {
        // Setup
        // Configure DkmSystemConfigurationExpiredMapper.selectOne(...).
        final DkmSystemConfigurationExpired dkmSystemConfigurationExpired = new DkmSystemConfigurationExpired(0,
                "username", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockDkmSystemConfigurationExpiredMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmSystemConfigurationExpired);

        // Run the test
        final LocalDate result = dkmSystemConfigurationExpiredServiceImplUnderTest.getDeadLine();

        // Verify the results
        //assertThat(result).isEqualTo(LocalDate.of(2020, 1, 1));
    }
}
