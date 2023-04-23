package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmVehicleCalibrationData;
import com.vecentek.back.mapper.DkmVehicleCalibrationDataMapper;
import com.vecentek.back.util.RedisUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmVehicleCalibrationDataServiceImplTest {

    @Mock
    private DkmVehicleCalibrationDataMapper mockDkmVehicleCalibrationDataMapper;
    @Mock
    private RedisUtils mockRedisUtils;

    @InjectMocks
    private DkmVehicleCalibrationDataServiceImpl dkmVehicleCalibrationDataServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmVehicleCalibrationData.class);
    }
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmVehicleCalibrationDataMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmVehicleCalibrationDataServiceImplUnderTest.selectForPage(0, 0, "vehicleModel", 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testUpdateDkmVehicleCalibrationDataById() {
        // Setup
        final DkmVehicleCalibrationData dkmVehicleCalibrationData = new DkmVehicleCalibrationData(0L, "vehicleModel",
                "1", "00006950686f6e65ffffffff6950686f6e6531332c34fffffffffffffff10001", "remarks", "1");
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmVehicleCalibrationDataMapper.updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmVehicleCalibrationDataServiceImplUnderTest.updateDkmVehicleCalibrationDataById(
                dkmVehicleCalibrationData);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmVehicleCalibrationDataMapper).updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
    }

    @Test
    void testImportByExcel() {
        // Setup
        final MultipartFile file = null;
        final PageResp expectedResult = PageResp.success("文件不能为空！");

        // Configure DkmVehicleCalibrationDataMapper.selectOne(...).
        final DkmVehicleCalibrationData dkmVehicleCalibrationData = new DkmVehicleCalibrationData(0L, "vehicleModel",
                "level", "vehicleAndCalibrationString", "remarks", "deleteFlag");
        when(mockDkmVehicleCalibrationDataMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmVehicleCalibrationData);

        when(mockDkmVehicleCalibrationDataMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmVehicleCalibrationDataMapper.insert(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(0);
        when(mockDkmVehicleCalibrationDataMapper.updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(0);
        when(mockDkmVehicleCalibrationDataMapper.insertPhoneCalibrationDataBatch(Arrays.asList(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag")))).thenReturn(0);

        // Run the test
        final PageResp result = dkmVehicleCalibrationDataServiceImplUnderTest.importByExcel(file);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmVehicleCalibrationDataMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockRedisUtils).setCacheObject("default", "vehicleAndCalibrationString");
        verify(mockDkmVehicleCalibrationDataMapper).insert(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
        verify(mockDkmVehicleCalibrationDataMapper).updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
    }

    @Test
    void testImportByExcel_DkmVehicleCalibrationDataMapperSelectOneReturnsNull() {
        // Setup
        final MultipartFile file = null;
        final PageResp expectedResult = PageResp.success("文件不能为空！");
        when(mockDkmVehicleCalibrationDataMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(mockDkmVehicleCalibrationDataMapper.insert(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(0);
        when(mockDkmVehicleCalibrationDataMapper.updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(0);
        when(mockDkmVehicleCalibrationDataMapper.insertPhoneCalibrationDataBatch(Arrays.asList(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag")))).thenReturn(0);

        // Run the test
        final PageResp result = dkmVehicleCalibrationDataServiceImplUnderTest.importByExcel(file);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockRedisUtils).setCacheObject("default", "vehicleAndCalibrationString");
        verify(mockDkmVehicleCalibrationDataMapper).insert(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
        verify(mockDkmVehicleCalibrationDataMapper).updateById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
    }

    @Test
    void testDownloadCalibrationExcel() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Configure DkmVehicleCalibrationDataMapper.selectList(...).
        final List<DkmVehicleCalibrationData> dkmVehicleCalibrationData = Arrays.asList(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"));
        when(mockDkmVehicleCalibrationDataMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmVehicleCalibrationData);

        // Run the test
        dkmVehicleCalibrationDataServiceImplUnderTest.downloadCalibrationExcel("vehicleModel", 0, false, response);

        // Verify the results
    }

    @Test
    void testDownloadCalibrationExcel_DkmVehicleCalibrationDataMapperReturnsNoItems() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();
        when(mockDkmVehicleCalibrationDataMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        dkmVehicleCalibrationDataServiceImplUnderTest.downloadCalibrationExcel("vehicleModel", 0, false, response);

        // Verify the results
    }
}
