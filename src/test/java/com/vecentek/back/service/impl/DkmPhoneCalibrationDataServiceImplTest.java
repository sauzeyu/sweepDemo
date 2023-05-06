package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.mapper.DkmPhoneCalibrationDataMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmPhoneCalibrationDataServiceImplTest {

    @Mock
    private DkmPhoneCalibrationDataMapper mockDkmPhoneCalibrationDataMapper;
    @Mock
    private RedisUtils mockRedisUtils;

    @InjectMocks
    private DkmPhoneCalibrationDataServiceImpl dkmPhoneCalibrationDataServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmPhoneCalibrationData.class);
    }
    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmPhoneCalibrationDataMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmPhoneCalibrationDataServiceImplUnderTest.selectForPage(0, 0, "phoneBrand",
                "vehicleModel", "vehicleType", "vehicleBrand");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testUpdateDkmPhoneCalibrationDataById() {
        // Setup
        final DkmPhoneCalibrationData dkmPhoneCalibrationData = new DkmPhoneCalibrationData(0L, "vehicleModel",
                "phoneBrand", "phoneModel", "00006950686f6e65ffffffff6950686f6e6531332c34fffffffffffffffff003", "remarks", "vehicleType", "vehicleBrand","featureData");
        final PageResp expectedResult = PageResp.success("更新成功");
        when(mockDkmPhoneCalibrationDataMapper.updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmPhoneCalibrationDataServiceImplUnderTest.updateDkmPhoneCalibrationDataById(
                dkmPhoneCalibrationData);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmPhoneCalibrationDataMapper).updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
    }

    @Test
    void testImportByExcel() throws IOException {
        // Setup
        byte[] bytes = {1, 2, 3, 4, 5};  // 创建大小为 5 的 byte 数组，并初始化元素为 1, 2, 3, 4, 5

        final MultipartFile file = new MockMultipartFile("test", bytes);

        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmPhoneCalibrationDataMapper.selectOne(...).
        final DkmPhoneCalibrationData dkmPhoneCalibrationData = new DkmPhoneCalibrationData(0L, "vehicleModel",
                "phoneBrand", "phoneModel", "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData");
        when(mockDkmPhoneCalibrationDataMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmPhoneCalibrationData);

        when(mockDkmPhoneCalibrationDataMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmPhoneCalibrationDataMapper.insert(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"))).thenReturn(0);
        when(mockDkmPhoneCalibrationDataMapper.updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"))).thenReturn(0);
        when(mockDkmPhoneCalibrationDataMapper.insertPhoneCalibrationDataBatch(Arrays.asList(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData")))).thenReturn(0);


        //when(ExcelUtil.getReader((File) any())).thenReturn(ExcelUtil.getReader(file.getInputStream()));
        // Run the test
        final PageResp result = dkmPhoneCalibrationDataServiceImplUnderTest.importByExcel(file);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmPhoneCalibrationDataMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockRedisUtils).setCacheObject("default", "personalAndCalibrationString");
        verify(mockDkmPhoneCalibrationDataMapper).insert(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
        verify(mockDkmPhoneCalibrationDataMapper).updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
    }

    @Test
    void testImportByExcel_DkmPhoneCalibrationDataMapperSelectOneReturnsNull() {
        // Setup
        final MultipartFile file = null;
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmPhoneCalibrationDataMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(mockDkmPhoneCalibrationDataMapper.insert(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"))).thenReturn(0);
        when(mockDkmPhoneCalibrationDataMapper.updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"))).thenReturn(0);
        when(mockDkmPhoneCalibrationDataMapper.insertPhoneCalibrationDataBatch(Arrays.asList(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData")))).thenReturn(0);

        // Run the test
        final PageResp result = dkmPhoneCalibrationDataServiceImplUnderTest.importByExcel(file);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockRedisUtils).setCacheObject("default", "personalAndCalibrationString");
        verify(mockDkmPhoneCalibrationDataMapper).insert(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
        verify(mockDkmPhoneCalibrationDataMapper).updateById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
    }

    @Test
    void testDownloadCalibrationExcel() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Configure DkmPhoneCalibrationDataMapper.selectList(...).
        final List<DkmPhoneCalibrationData> dkmPhoneCalibrationData = Arrays.asList(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand","featureData"));
        when(mockDkmPhoneCalibrationDataMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmPhoneCalibrationData);

        // Run the test
        dkmPhoneCalibrationDataServiceImplUnderTest.downloadCalibrationExcel("phoneBrand", "vehicleModel",
                "vehicleType", "vehicleBrand", false, response);

        // Verify the results
    }

    @Test
    void testDownloadCalibrationExcel_DkmPhoneCalibrationDataMapperReturnsNoItems() throws Exception {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();
        when(mockDkmPhoneCalibrationDataMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        dkmPhoneCalibrationDataServiceImplUnderTest.downloadCalibrationExcel("phoneBrand", "vehicleModel",
                "vehicleType", "vehicleBrand", false, response);

        // Verify the results
    }
}
