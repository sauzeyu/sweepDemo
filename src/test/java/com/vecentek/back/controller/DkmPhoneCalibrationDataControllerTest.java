package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.service.impl.DkmPhoneCalibrationDataServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmPhoneCalibrationDataController.class)
class DkmPhoneCalibrationDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmPhoneCalibrationDataServiceImpl mockDkmPhoneCalibrationDataServiceImpl;

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.selectForPage(0, 0, "phoneBrand", "vehicleModel", "vehicleType",
                "vehicleBrand")).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmPhoneCalibrationData/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("phoneBrand", "phoneBrand")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleType", "vehicleType")
                .param("vehicleBrand", "vehicleBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectForPage_DkmPhoneCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.selectForPage(0, 0, "phoneBrand", "vehicleModel", "vehicleType",
                "vehicleBrand")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmPhoneCalibrationData/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("phoneBrand", "phoneBrand")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleType", "vehicleType")
                .param("vehicleBrand", "vehicleBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdateById() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.updateDkmPhoneCalibrationDataById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand")))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmPhoneCalibrationData/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdateById_DkmPhoneCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.updateDkmPhoneCalibrationDataById(
                new DkmPhoneCalibrationData(0L, "vehicleModel", "phoneBrand", "phoneModel",
                        "personalAndCalibrationString", "remarks", "vehicleType", "vehicleBrand")))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmPhoneCalibrationData/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testImportByExcel() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.importByExcel(any(MultipartFile.class)))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(multipart("/dkmPhoneCalibrationData/importByExcel")
                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                        "content".getBytes()))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testImportByExcel_DkmPhoneCalibrationDataServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmPhoneCalibrationDataServiceImpl.importByExcel(any(MultipartFile.class)))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(multipart("/dkmPhoneCalibrationData/importByExcel")
                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                        "content".getBytes()))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testDownloadCalibrationExcel() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmPhoneCalibrationData/downloadCalibrationExcel")
                        .param("phoneBrand", "phoneBrand")
                        .param("vehicleModel", "vehicleModel")
                        .param("vehicleType", "vehicleType")
                        .param("vehicleBrand", "vehicleBrand")
                        .param("isXlsx", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockDkmPhoneCalibrationDataServiceImpl).downloadCalibrationExcel(eq("phoneBrand"), eq("vehicleModel"),
                eq("vehicleType"), eq("vehicleBrand"), eq(false), any(HttpServletResponse.class));
    }

    @Test
    void testDownloadCalibrationExcel_DkmPhoneCalibrationDataServiceImplThrowsUnsupportedEncodingException() throws Exception {
        // Setup
        doThrow(UnsupportedEncodingException.class).when(
                mockDkmPhoneCalibrationDataServiceImpl).downloadCalibrationExcel(eq("phoneBrand"), eq("vehicleModel"),
                eq("vehicleType"), eq("vehicleBrand"), eq(false), any(HttpServletResponse.class));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmPhoneCalibrationData/downloadCalibrationExcel")
                        .param("phoneBrand", "phoneBrand")
                        .param("vehicleModel", "vehicleModel")
                        .param("vehicleType", "vehicleType")
                        .param("vehicleBrand", "vehicleBrand")
                        .param("isXlsx", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
