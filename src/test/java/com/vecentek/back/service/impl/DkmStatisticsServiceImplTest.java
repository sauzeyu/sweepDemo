package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.config.ProConfig;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmStatisticsServiceImplTest {

    @Mock
    private DkmVehicleMapper mockDkmVehicleMapper;
    @Mock
    private DkmKeyMapper mockDkmKeyMapper;
    @Mock
    private DkmKeyLogMapper mockDkmKeyLogMapper;
    private MockedStatic springContextUtilMocked;

    @InjectMocks
    private DkmStatisticsServiceImpl dkmStatisticsServiceImplUnderTest;

    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKey.class);
        MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        springContextUtilMocked = springContextUtilMockedStatic;
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
    }


    @AfterEach
    public void afterEach() {
        springContextUtilMocked.close();
    }
    @Test
    void testSelectTotal() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmKeyMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmKeyLogMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectTotal(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetTime() {

        when(dkmStatisticsServiceImplUnderTest.getTime()).thenReturn(new Date[]{new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()});
        assertThat(dkmStatisticsServiceImplUnderTest.getTime())
                .isEqualTo(new Date[]{new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()});
    }

    @Test
    void testSelectVehicleAndKeyAndKeyLogTotal() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectCount(any(Wrapper.class))).thenReturn(0);
        when(mockDkmKeyMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmKeyLogMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        //MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectVehicleAndKeyAndKeyLogTotal();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyLogByMonth() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectUseLogCountByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmKeyLogMapper.selectUseLogCountByMonth("startTime", "endTime")).thenReturn(monthCountDTOS);

        // Configure DkmKeyLogMapper.selectErrorLogCountByMonth(...).
        final MonthCountDTO monthCountDTO1 = new MonthCountDTO();
        monthCountDTO1.setMonth("month");
        monthCountDTO1.setCount(0);
        final List<MonthCountDTO> monthCountDTOS1 = Arrays.asList(monthCountDTO1);
        when(mockDkmKeyLogMapper.selectErrorLogCountByMonth("startTime", "endTime")).thenReturn(monthCountDTOS1);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyLogByMonth();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyLogByMonth_DkmKeyLogMapperSelectUseLogCountByMonthReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectUseLogCountByMonth("startTime", "endTime")).thenReturn(Collections.emptyList());

        // Configure DkmKeyLogMapper.selectErrorLogCountByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmKeyLogMapper.selectErrorLogCountByMonth("startTime", "endTime")).thenReturn(monthCountDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyLogByMonth();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyLogByMonth_DkmKeyLogMapperSelectErrorLogCountByMonthReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectUseLogCountByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmKeyLogMapper.selectUseLogCountByMonth("startTime", "endTime")).thenReturn(monthCountDTOS);

        when(mockDkmKeyLogMapper.selectErrorLogCountByMonth("startTime", "endTime"))
                .thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyLogByMonth();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyUseLogByTime() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectUseCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectUseCountByTime("startTime", "endTime")).thenReturn(countDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyUseLogByTime("startTime", "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyUseLogByTime_DkmKeyLogMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectUseCountByTime("startTime", "endTime")).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyUseLogByTime("startTime", "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByTime() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectErrorCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectErrorCountByTime("startTime", "endTime")).thenReturn(countDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByTime("startTime", "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByTime_DkmKeyLogMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectErrorCountByTime("startTime", "endTime")).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByTime("startTime", "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByPhoneBrand() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectKeyErrorLog(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectKeyErrorLog(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        // Configure DkmKeyLogMapper.selectKeyErrorLogByPhoneBrand(...).
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> countDTOS1 = Arrays.asList(countDTO1);
        when(mockDkmKeyLogMapper.selectKeyErrorLogByPhoneBrand("phoneBrand",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS1);
        //MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByPhoneBrand("phoneBrand");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByPhoneBrand_DkmKeyLogMapperSelectKeyErrorLogReturnsNoItems() {
        // Setup
        //MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectKeyErrorLog(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByPhoneBrand("phoneBrand");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByPhoneBrand_DkmKeyLogMapperSelectKeyErrorLogByPhoneBrandReturnsNoItems() {
        // Setup
        //MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectKeyErrorLogByPhoneBrand("phoneBrand",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByPhoneBrand("phoneBrand");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testVehicleStatistics() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmVehicleMapper.selectCountByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmVehicleMapper.selectCountByMonth()).thenReturn(monthCountDTOS);

        when(mockDkmVehicleMapper.selectNewToday()).thenReturn(0);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.vehicleStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testVehicleStatistics_DkmVehicleMapperSelectCountByMonthReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectCountByMonth()).thenReturn(Collections.emptyList());
        when(mockDkmVehicleMapper.selectNewToday()).thenReturn(0);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.vehicleStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testKeyStatistics() {
        // Setup
        //MockedStatic<SpringContextUtil> springContextUtilMockedStatic = Mockito.mockStatic(SpringContextUtil.class);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.keyStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectErrorStatusTotal() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectPhoneErrorCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectPhoneErrorCountByTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        // Configure DkmKeyLogMapper.selectStatusErrorCountByTime(...).
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> countDTOS1 = Arrays.asList(countDTO1);
        when(mockDkmKeyLogMapper.selectStatusErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS1);

        // Configure DkmKeyLogMapper.selectVehicleErrorCountByTime(...).
        final CountDTO countDTO2 = new CountDTO();
        countDTO2.setValue(0);
        countDTO2.setName("name");
        final List<CountDTO> countDTOS2 = Arrays.asList(countDTO2);
        when(mockDkmKeyLogMapper.selectVehicleErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS2);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectErrorStatusTotal(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectErrorStatusTotal_DkmKeyLogMapperSelectPhoneErrorCountByTimeReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectPhoneErrorCountByTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Configure DkmKeyLogMapper.selectStatusErrorCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectStatusErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        // Configure DkmKeyLogMapper.selectVehicleErrorCountByTime(...).
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> countDTOS1 = Arrays.asList(countDTO1);
        when(mockDkmKeyLogMapper.selectVehicleErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS1);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectErrorStatusTotal(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectErrorStatusTotal_DkmKeyLogMapperSelectStatusErrorCountByTimeReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectPhoneErrorCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectPhoneErrorCountByTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        when(mockDkmKeyLogMapper.selectStatusErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Configure DkmKeyLogMapper.selectVehicleErrorCountByTime(...).
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> countDTOS1 = Arrays.asList(countDTO1);
        when(mockDkmKeyLogMapper.selectVehicleErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS1);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectErrorStatusTotal(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectErrorStatusTotal_DkmKeyLogMapperSelectVehicleErrorCountByTimeReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectPhoneErrorCountByTime(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectPhoneErrorCountByTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        // Configure DkmKeyLogMapper.selectStatusErrorCountByTime(...).
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> countDTOS1 = Arrays.asList(countDTO1);
        when(mockDkmKeyLogMapper.selectStatusErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS1);

        when(mockDkmKeyLogMapper.selectVehicleErrorCountByTime(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectErrorStatusTotal(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testKeyUseTimeStatistics() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.countUseToday("now", "lastDay")).thenReturn(0);

        // Configure DkmVehicleMapper.countUseByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmVehicleMapper.countUseByMonth("yearFirstDay", "yearLastDay")).thenReturn(monthCountDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.keyUseTimeStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testKeyUseTimeStatistics_DkmVehicleMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.countUseToday("now", "lastDay")).thenReturn(0);
        when(mockDkmVehicleMapper.countUseByMonth("yearFirstDay", "yearLastDay")).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.keyUseTimeStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testKeyErrorTimeStatistics() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.countErrorToday("now", "nextDay")).thenReturn(0);

        // Configure DkmVehicleMapper.countErrorByMonth(...).
        final MonthCountDTO monthCountDTO = new MonthCountDTO();
        monthCountDTO.setMonth("month");
        monthCountDTO.setCount(0);
        final List<MonthCountDTO> monthCountDTOS = Arrays.asList(monthCountDTO);
        when(mockDkmVehicleMapper.countErrorByMonth("yearFirstDay", "yearLastDay")).thenReturn(monthCountDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.keyErrorTimeStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testKeyErrorTimeStatistics_DkmVehicleMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.countErrorToday("now", "nextDay")).thenReturn(0);
        when(mockDkmVehicleMapper.countErrorByMonth("yearFirstDay", "yearLastDay")).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.keyErrorTimeStatistics();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByAllPhoneBrand() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogMapper.selectKeyErrorLogByAllPhoneBrand(...).
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOS = Arrays.asList(countDTO);
        when(mockDkmKeyLogMapper.selectKeyErrorLogByAllPhoneBrand(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(countDTOS);

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByAllPhoneBrand();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectKeyErrorLogByAllPhoneBrand_DkmKeyLogMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogMapper.selectKeyErrorLogByAllPhoneBrand(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmStatisticsServiceImplUnderTest.selectKeyErrorLogByAllPhoneBrand();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSimpleLog() {
        // Setup
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("st00");
        final List<CountDTO> countDTOs = Arrays.asList(countDTO);
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("st01");
        final List<CountDTO> expectedResult = Arrays.asList(countDTO1);

        // Run the test
        final List<CountDTO> result = dkmStatisticsServiceImplUnderTest.simpleLog(countDTOs, "st");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSimpleLog2() {
        // Setup
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("0C00");
        countDTO.setValue(0);
        countDTO.setName("0A00");
        countDTO.setValue(0);
        countDTO.setName("0C0C");
        final List<CountDTO> countDTOs = Arrays.asList(countDTO);
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("0C13");
        final List<CountDTO> expectedResult = Arrays.asList(countDTO1);

        // Run the test
        final List<CountDTO> result = dkmStatisticsServiceImplUnderTest.simpleLog2(countDTOs, "0C");
        final List<CountDTO> result1 = dkmStatisticsServiceImplUnderTest.simpleLog2(expectedResult, "0C");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testTwoBytesLog() {
        // Setup
        final CountDTO countDTO = new CountDTO();
        countDTO.setValue(0);
        countDTO.setName("name");
        final List<CountDTO> countDTOs = Arrays.asList(countDTO);
        final CountDTO countDTO1 = new CountDTO();
        countDTO1.setValue(0);
        countDTO1.setName("name");
        final List<CountDTO> expectedResult = Arrays.asList(countDTO1);

        // Run the test
        final List<CountDTO> result = dkmStatisticsServiceImplUnderTest.twoBytesLog(countDTOs);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
