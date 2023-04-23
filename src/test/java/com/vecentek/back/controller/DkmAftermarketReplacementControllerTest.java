package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmAftermarketReplacementServiceImpl;
import com.vecentek.common.response.PageResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmAftermarketReplacementController.class)
class DkmAftermarketReplacementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmAftermarketReplacementServiceImpl mockDkmAftermarketReplacementService;

    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {

         //Setup
        when(mockDkmAftermarketReplacementService.selectForPage(1, 10, "LGQIZ44O82E7T9994", "2022-10-20 17:15:47", "2022-10-22 17:15:47"))
                .thenReturn(PageResp.success("查询成功", 10L, new ArrayList<>()));


        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectForPage")
                .param("pageIndex", "1")
                .param("pageSize", "10")
                .param("vin", "LGQIZ44O82E7T9994")
                .param("startTime", "2022-10-20 17:15:47")
                .param("endTime", "2022-10-22 17:15:47")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo("{\"code\":200,\"msg\":\"查询成功\",\"total\":10}");
    }

    @Test
    void testSelectForPage_DkmAftermarketReplacementServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAftermarketReplacementService.selectForPage(0, 0, "vin", "startTime", "endTime"))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vin", "vin")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectByVin() throws Exception {
        // Setup
        when(mockDkmAftermarketReplacementService.selectByVin(0, 0, "vin")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectByVin")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectByVin_DkmAftermarketReplacementServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAftermarketReplacementService.selectByVin(0, 0, "vin")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectByVin")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectVehicleByVin() throws Exception {
        // Setup
        when(mockDkmAftermarketReplacementService.selectVehicleByVin("vin")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectVehicleByVin")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectVehicleByVin_DkmAftermarketReplacementServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAftermarketReplacementService.selectVehicleByVin("vin")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAftermarketReplacement/selectVehicleByVin")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testDownloadAftermarketReplacement() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmAftermarketReplacement/downloadAftermarketReplacement")
                        .param("vin", "vin")
                        .param("startTime", "startTime")
                        .param("endTime", "endTime")
                        .param("isXls", "false")
                        .param("creator", "creator")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(mockDkmAftermarketReplacementService).downloadAftermarketReplacement(eq("vin"), eq("startTime"),
                eq("endTime"), eq(false), eq("creator"), any(HttpServletResponse.class));
    }

    @Test
    void testDownloadAftermarketReplacement_DkmAftermarketReplacementServiceImplThrowsUnsupportedEncodingException() throws Exception {
        // Setup
        doThrow(UnsupportedEncodingException.class).when(
                mockDkmAftermarketReplacementService).downloadAftermarketReplacement(eq("vin"), eq("startTime"),
                eq("endTime"), eq(false), eq("creator"), any(HttpServletResponse.class));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmAftermarketReplacement/downloadAftermarketReplacement")
                        .param("vin", "vin")
                        .param("startTime", "startTime")
                        .param("endTime", "endTime")
                        .param("isXls", "false")
                        .param("creator", "creator")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(fiveHundredMessageResponse);
    }
}
