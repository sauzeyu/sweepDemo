package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.config.ProConfig;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmKeyLogMapper;
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
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmKeyLogServiceImplTest {

    @Mock
    private DkmKeyLogMapper mockDkmKeyLogMapper;
    @Mock
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;
    private MockedStatic springContextUtilMocked;
    @InjectMocks
    private DkmKeyLogServiceImpl dkmKeyLogServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLog.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLogHistoryExport.class);
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
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        final DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setId(0L);
        dkmKeyLog.setVin("vin");
        dkmKeyLog.setKeyId("keyId");
        dkmKeyLog.setPhoneModel("phoneModel");
        dkmKeyLog.setUserId("userId");
        dkmKeyLog.setPhoneBrand("phoneBrand");
        dkmKeyLog.setFlag(0);
        dkmKeyLog.setFlagVO("失败");
        dkmKeyLog.setStatusCode("蓝牙断开");
        dkmKeyLog.setErrorReason("errorReason");
        dkmKeyLog.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        dkmKeyLog.setVehicleBrand("vehicleBrand");
        dkmKeyLog.setVehicleModel("vehicleModel");
        dkmKeyLog.setVehicleType("vehicleType");
        dkmKeyLog.setOperationType("operationType");
        final List<DkmKeyLog> dkmKeyLogs = Arrays.asList(dkmKeyLog);
        expectedResult.setData(dkmKeyLogs);
        Page<DkmKeyLog> objectPage = new Page<>(0L, 0L, 0L, false);
        objectPage.setRecords(dkmKeyLogs);
        when(mockDkmKeyLogMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(objectPage);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);
        // Run the test
        final PageResp result = dkmKeyLogServiceImplUnderTest.selectForPage(0, 0, "vin", "userId", null,
                null, "phoneBrand", "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel",
                "vehicleType");
        dkmKeyLog.setStatusCode("09");
        // Run the test
        final PageResp result1 = dkmKeyLogServiceImplUnderTest.selectForPage(0, 0, "vin", "userId", null,
                null, "phoneBrand", "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel",
                "vehicleType");
        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDownloadKeyLogExcel() {
        // Setup
        when(mockDkmKeyLogMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(100001);
        ProConfig proConfig = new ProConfig();
        proConfig.setSysDate("2021-01-01");
        springContextUtilMocked.when(() -> SpringContextUtil.getBean(ProConfig.class)).thenReturn(proConfig);

        // Configure DkmKeyLogMapper.selectList(...).
        final DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setId(0L);
        dkmKeyLog.setVin("vin");
        dkmKeyLog.setKeyId("keyId");
        dkmKeyLog.setPhoneModel("phoneModel");
        dkmKeyLog.setUserId("userId");
        dkmKeyLog.setPhoneBrand("phoneBrand");
        dkmKeyLog.setFlag(0);
        dkmKeyLog.setFlagVO("失败");
        dkmKeyLog.setStatusCode("statusCode");
        dkmKeyLog.setErrorReason("errorReason");
        dkmKeyLog.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        dkmKeyLog.setVehicleBrand("vehicleBrand");
        dkmKeyLog.setVehicleModel("vehicleModel");
        dkmKeyLog.setVehicleType("vehicleType");
        dkmKeyLog.setOperationType("operationType");
        final List<DkmKeyLog> dkmKeyLogs = Arrays.asList(dkmKeyLog);
        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeyLogs);

        when(mockDkmKeyLogHistoryExportMapper.update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class))).thenReturn(0);

        // Run the test
        dkmKeyLogServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType", "excelName");
        dkmKeyLog.setStatusCode("蓝牙断开");
        dkmKeyLog.setFlag(1);
        // Run the test
        dkmKeyLogServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType", "excelName");
        // Verify the results
        //verify(mockDkmKeyLogHistoryExportMapper).update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class));
    }

    @Test
    void testDownloadKeyLogExcel_DkmKeyLogMapperSelectListReturnsNoItems() {
        // Setup
        when(mockDkmKeyLogMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(100001);
        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(mockDkmKeyLogHistoryExportMapper.update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class))).thenReturn(0);

        // Run the test
        dkmKeyLogServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType", "excelName");

        // Verify the results
        //verify(mockDkmKeyLogHistoryExportMapper).update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class));
    }
}
