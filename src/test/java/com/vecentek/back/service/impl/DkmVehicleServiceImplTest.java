package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.DkmVehicleDTO;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmUserVehicleMapper;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmVehicleServiceImplTest {

    @Mock
    private DkmVehicleMapper mockDkmVehicleMapper;
    @Mock
    private DkmUserVehicleMapper mockDkmUserVehicleMapper;
    @Mock
    private DkmUserMapper mockDkmUserMapper;

    @InjectMocks
    private DkmVehicleServiceImpl dkmVehicleServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmVehicle.class);
    }
    @Test
    void testSelectById() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmVehicleMapper.selectById(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn","hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectById(0)).thenReturn(dkmVehicle);

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectForPage(0, 0, "vin", "hwDeviceSn", "vehicleModel",
                "vehicleBrand", "vehicleType");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByUserId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectForCountByUserId(0)).thenReturn(0L);

        // Configure DkmVehicleMapper.selectForPageByUserId(...).
        final List<DkmVehicleDTO> dkmVehicleDTOS = Arrays.asList(new DkmVehicleDTO("userId", "vehicleType", "license"));
        when(mockDkmVehicleMapper.selectForPageByUserId(0, 0, 0)).thenReturn(dkmVehicleDTOS);

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectForPageByUserId(0, 0, 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByUserId_DkmVehicleMapperSelectForPageByUserIdReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleMapper.selectForCountByUserId(0)).thenReturn(0L);
        when(mockDkmVehicleMapper.selectForPageByUserId(0, 0, 0)).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectForPageByUserId(0, 0, 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testInsert() {
        // Setup
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn","hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        final PageResp expectedResult = PageResp.success("新增失败");
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn","hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.insert(dkmVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testUpdateById() {
        // Setup
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "hwDeviceSn","searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        final PageResp expectedResult = PageResp.success("更新失败");
        when(mockDkmVehicleMapper.updateById(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn","hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.updateById(dkmVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectUserByVehicleId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "userId",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "phone", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        when(mockDkmUserMapper.selectById("userId")).thenReturn(new DkmUser("id", "phone", "username"));

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectUserByVehicleId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectUserByVehicleId_DkmUserVehicleMapperReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询失败");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Run the test
        final PageResp result = dkmVehicleServiceImplUnderTest.selectUserByVehicleId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDownloadDkmVehicle() {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Configure DkmVehicleMapper.selectList(...).
        final List<DkmVehicle> dkmVehicles = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn","hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles);

        // Run the test
        dkmVehicleServiceImplUnderTest.downloadDkmVehicle("vin", "hwDeviceSn", "vehicleModel", "vehicleBrand",
                "vehicleType", response);

        // Verify the results
    }

    @Test
    void testDownloadDkmVehicle_DkmVehicleMapperReturnsNoItems() {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        dkmVehicleServiceImplUnderTest.downloadDkmVehicle("vin", "hwDeviceSn", "vehicleModel", "vehicleBrand",
                "vehicleType", response);

        // Verify the results
    }
}
