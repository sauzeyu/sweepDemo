package com.vecentek.back.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.entity.DkmVehicleCalibrationData;
import com.vecentek.back.mapper.DkmPhoneCalibrationDataMapper;
import com.vecentek.back.mapper.DkmVehicleCalibrationDataMapper;
import com.vecentek.back.util.RedisUtils;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmVehicleCalibrationDataServiceImplTest {

    @Mock
    private DkmVehicleCalibrationDataMapper mockDkmVehicleCalibrationDataMapper;
    @Mock
    private RedisUtils mockRedisUtils;

    @Mock
    private DkmPhoneCalibrationDataServiceImpl dkmPhoneCalibrationDataService;
    @Mock
    private DkmPhoneCalibrationDataMapper dkmPhoneCalibrationDataMapper;

    @Spy
    private DkmVehicleCalibrationDataServiceImpl dkmVehicleCalibrationDataServiceImplUnderTestSPY;
    @Mock
    ExcelReader mockExcelReader = null;

private MockedStatic excelUtilMocked;
    @InjectMocks
    private DkmVehicleCalibrationDataServiceImpl dkmVehicleCalibrationDataServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmVehicleCalibrationData.class);
        MockedStatic<ExcelUtil> excelUtilMockedStatic = Mockito.mockStatic(ExcelUtil.class);
        excelUtilMocked = excelUtilMockedStatic;
    }
    @AfterEach
    public void afterEach() {
        excelUtilMocked.close();
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
    void testImportByExcel() throws IOException {
        // Setup
      List<DkmPhoneCalibrationData> dkmPhoneCalibrationData = Arrays.asList(
                new DkmPhoneCalibrationData(123L,"vehicleModel","phoneBrand","phoneModel","personalAndCalibrationString","remarks",
                        "vehicleType","vehicleBrand","featureData"));
        // 构造MultipartFile对象
        // 构造 Excel 文件
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet1");
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("车辆型号");
        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("手机品牌");
        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("手机型号");
        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("车辆品牌");
        XSSFCell cell4 = row.createCell(4);
        cell4.setCellValue("车型");
        XSSFCell cell5 = row.createCell(5);
        cell5.setCellValue("标定数据");
        XSSFCell cell6 = row.createCell(6);
        cell6.setCellValue("特征点数据");
        XSSFCell cell7 = row.createCell(7);
        cell7.setCellValue("蓝牙灵敏度等级");
        XSSFRow row1 = sheet.createRow(1);
        XSSFCell cell10 = row1.createCell(0);
        cell10.setCellValue("zs123");
        XSSFCell cell11 = row1.createCell(1);
        cell11.setCellValue("iPhone");
        XSSFCell cell12 = row1.createCell(2);
        cell12.setCellValue("iPhone 12 Pro Max");
        XSSFCell cell13 = row1.createCell(3);
        cell13.setCellValue("福田");
        XSSFCell cell14 = row1.createCell(4);
        cell14.setCellValue("default");
        XSSFCell cell15 = row1.createCell(5);
        cell15.setCellValue("00006950686f6e65ffffffff6950686f6e6531332c34ffffffffffffffffffff");
        XSSFCell cell16 = row1.createCell(6);
        cell16.setCellValue("ffffffd4d3d20affffffffffffffffffffffffff7fba9f3bc89c6daa7d7eb85b4bc86dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        XSSFCell cell17 = row1.createCell(7);
        cell17.setCellValue(1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wb.write(outputStream);
        byte[] content = outputStream.toByteArray();

        // 构造 MultipartFile 文件
        MultipartFile multipartFile = new MockMultipartFile("test.xlsx", "test.xlsx",
                "application/vnd.ms-excel", new ByteArrayInputStream(content));


        final PageResp expectedResult = PageResp.success("文件不能为空！");
        System.out.println(multipartFile);
        ExcelReader reader = new ExcelReader(multipartFile.getInputStream(), "Sheet1");
        mockExcelReader = reader;

        excelUtilMocked.when(() -> ExcelUtil.getReader((InputStream) any())).thenReturn(mockExcelReader);


        //doReturn(dkmPhoneCalibrationData).when(mockExcelReader).readAll(DkmPhoneCalibrationData.class);
        //when(mockExcelReader.readAll(DkmPhoneCalibrationData.class)).thenReturn(dkmPhoneCalibrationData);
        //doReturn(dkmPhoneCalibrationData).when(mockExcelReader).readAll(DkmPhoneCalibrationData.class);

        // Configure DkmVehicleCalibrationDataMapper.selectOne(...).
        final DkmVehicleCalibrationData dkmVehicleCalibrationData = new DkmVehicleCalibrationData(0L, "vehicleModel",
                "level", "vehicleAndCalibrationString", "remarks", "deleteFlag");
        doReturn(dkmVehicleCalibrationData)
                .when(mockDkmVehicleCalibrationDataMapper)
                .selectOne(any(LambdaQueryWrapper.class));

        doReturn(0)
                .when(mockDkmVehicleCalibrationDataMapper)
                .delete(any(LambdaQueryWrapper.class));

        doReturn(0)
                .when(mockDkmVehicleCalibrationDataMapper)
                .insert(
                        new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                                "deleteFlag"));

        doReturn(0)
                .when(mockDkmVehicleCalibrationDataMapper)
                .updateById(
                        new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                                "deleteFlag"));

        doReturn(0)
                .when(mockDkmVehicleCalibrationDataMapper)
                .insertPhoneCalibrationDataBatch(
                        Arrays.asList(
                                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                                        "deleteFlag")
                        )
                );


        // Run the test
        final PageResp result = dkmVehicleCalibrationDataServiceImplUnderTest.importByExcel(multipartFile);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());

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

        ExcelWriter writer = new ExcelWriter();

        excelUtilMocked.when(() -> ExcelUtil.getWriter(anyBoolean())).thenReturn(writer);
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
