package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmVehicleCalibrationData;
import com.vecentek.back.service.impl.DkmPhoneCalibrationDataServiceImpl;
import com.vecentek.back.service.impl.DkmVehicleCalibrationDataServiceImpl;
import com.vecentek.common.response.PageResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmVehicleCalibrationDataController.class)
class DkmVehicleCalibrationDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmPhoneCalibrationDataServiceImpl mockDkmPhoneCalibrationDataServiceImpl;
    @MockBean
    private DkmVehicleCalibrationDataServiceImpl mockDkmVehicleCalibrationDataServiceImpl;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.selectForPage(0, 0, "vehicleModel", 0))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicleCalibrationData/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vehicleModel", "vehicleModel")
                .param("level", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmVehicleCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.selectForPage(0, 0, "vehicleModel", 0))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicleCalibrationData/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vehicleModel", "vehicleModel")
                .param("level", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testUpdateById() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.updateDkmVehicleCalibrationDataById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicleCalibrationData/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testUpdateById_DkmVehicleCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.updateDkmVehicleCalibrationDataById(
                new DkmVehicleCalibrationData(0L, "vehicleModel", "level", "vehicleAndCalibrationString", "remarks",
                        "deleteFlag"))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicleCalibrationData/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testImportByExcel() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.importByExcel(any(MultipartFile.class)))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(multipart("/dkmVehicleCalibrationData/importByExcel")
                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                        "content".getBytes()))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testImportByExcel_DkmVehicleCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleCalibrationDataServiceImpl.importByExcel(any(MultipartFile.class)))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(multipart("/dkmVehicleCalibrationData/importByExcel")
                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                        "content".getBytes()))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testDownloadCalibrationExcel() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmVehicleCalibrationData/downloadCalibrationExcel")
                        .param("vehicleModel", "vehicleModel")
                        .param("level", "0")
                        .param("isXlsx", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo("");
        verify(mockDkmVehicleCalibrationDataServiceImpl).downloadCalibrationExcel(eq("vehicleModel"), eq(0), eq(false),
                any(HttpServletResponse.class));
    }

    @Test
    void testDownloadCalibrationExcel_DkmVehicleCalibrationDataServiceImplThrowsUnsupportedEncodingException() throws Exception {
        // Setup
        doThrow(UnsupportedEncodingException.class).when(
                mockDkmVehicleCalibrationDataServiceImpl).downloadCalibrationExcel(eq("vehicleModel"), eq(0), eq(false),
                any(HttpServletResponse.class));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmVehicleCalibrationData/downloadCalibrationExcel")
                        .param("vehicleModel", "vehicleModel")
                        .param("level", "0")
                        .param("isXlsx", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredMessageResponse);
    }
}
