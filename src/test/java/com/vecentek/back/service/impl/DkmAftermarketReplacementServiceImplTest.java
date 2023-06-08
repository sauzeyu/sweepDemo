package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAftermarketReplacement;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmAftermarketReplacementMapper;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class DkmAftermarketReplacementServiceImplTest {

    @Mock
    private DkmAftermarketReplacementMapper mockDkmAftermarketReplacementMapper;
    @Mock
    private DkmBluetoothsMapper mockDkmBluetoothsMapper;
    @Mock
    private DkmVehicleMapper mockDkmVehicleMapper;
    @Mock
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;

    @InjectMocks
    private DkmAftermarketReplacementServiceImpl dkmAftermarketReplacementServiceImplUnderTest;

    @BeforeEach
    public void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmAftermarketReplacement.class);

    }
    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success();
        when(mockDkmAftermarketReplacementMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmAftermarketReplacementServiceImplUnderTest.selectForPage(0, 0, "vin", "startTime",
                "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectByVin() {
        // Setup
        Page<DkmAftermarketReplacement> objectPage = new Page<>(1,10);
        final PageResp expectedResult = PageResp.success();
        when(mockDkmAftermarketReplacementMapper.selectPage(any(), any()))
                .thenReturn(objectPage);

        // Run the test
        final PageResp result = dkmAftermarketReplacementServiceImplUnderTest.selectByVin(0, 0, "vin");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectVehicleByVin() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        // Run the test
        final PageResp result = dkmAftermarketReplacementServiceImplUnderTest.selectVehicleByVin("vin");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDownloadAftermarketReplacement() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Configure DkmAftermarketReplacementMapper.selectList(...).
        final List<DkmAftermarketReplacement> dkmAftermarketReplacements = Arrays.asList(
                new DkmAftermarketReplacement(0L, "vin", "oldBluetoothSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        doReturn(dkmAftermarketReplacements).when(mockDkmAftermarketReplacementMapper).selectList(any());

        doReturn(dkmAftermarketReplacements).when(mockDkmKeyLogHistoryExportMapper).insert(any());


        // Run the test
        dkmAftermarketReplacementServiceImplUnderTest.downloadAftermarketReplacement("vin", "startTime", "endTime",
                false, "creator", response);

        // Verify the results
        verify(mockDkmKeyLogHistoryExportMapper).insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0));
    }

    @Test
    void testDownloadAftermarketReplacement_DkmAftermarketReplacementMapperReturnsNoItems() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();
        when(mockDkmAftermarketReplacementMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.emptyList());
        when(mockDkmKeyLogHistoryExportMapper.insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0))).thenReturn(0);

        // Run the test
        dkmAftermarketReplacementServiceImplUnderTest.downloadAftermarketReplacement("vin", "startTime", "endTime",
                false, "creator", response);

        // Verify the results
        verify(mockDkmKeyLogHistoryExportMapper).insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0));
    }
}
