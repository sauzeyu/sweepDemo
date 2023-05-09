package com.vecentek.back.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
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
import javax.validation.Validator;
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
    @Mock
    private Validator validator;
    private static final int TEST_PAGE_SIZE = 10;
    private static final String TEST_UPLOAD_FILE_NAME = "test-upload-file.xls";
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
        cell.setCellValue("hello world");

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
        //doReturn(dkmPhoneCalibrationData).when(mockExcelReader).readAll(DkmPhoneCalibrationData.class);
        when(reader.readAll(DkmPhoneCalibrationData.class)).thenReturn(dkmPhoneCalibrationData);

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
