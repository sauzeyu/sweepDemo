package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmBluetoothsServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmBluetoothsController.class)
class DkmBluetoothsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmBluetoothsServiceImpl mockDkmBluetoothsServiceImpl;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmBluetoothsServiceImpl.selectForPage(0, 0, "hwDeviceSn", "searchNumber", 0))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmBluetooths/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("searchNumber", "searchNumber")
                .param("flag", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmBluetoothsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmBluetoothsServiceImpl.selectForPage(0, 0, "hwDeviceSn", "searchNumber", 0))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmBluetooths/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("searchNumber", "searchNumber")
                .param("flag", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testDeleteById() throws Exception {
        // Setup
        when(mockDkmBluetoothsServiceImpl.deleteById("hwDeviceSn")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmBluetooths/deleteById")
                .param("hwDeviceSn", "hwDeviceSn")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testDeleteById_DkmBluetoothsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmBluetoothsServiceImpl.deleteById("hwDeviceSn")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmBluetooths/deleteById")
                .param("hwDeviceSn", "hwDeviceSn")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testDownloadDkmBluetooths() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmBluetooths/downloadDkmBluetooths")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("searchNumber", "searchNumber")
                .param("flag", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(mockDkmBluetoothsServiceImpl).downloadDkmBluetooths(eq("hwDeviceSn"), eq("searchNumber"), eq(0),
                any(HttpServletResponse.class));
    }
}
