package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.service.impl.DkmKeyServiceImpl;
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

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmKeyController.class)
class DkmKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmKeyServiceImpl mockDkmKeyServiceImpl;
    @MockBean
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;

    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPage(eq(0), eq(0), eq("userId"), eq("vin"), eq(0), eq(0), eq("periodUnit"),
                eq("applyStartTime"), eq("applyEndTime"), eq("valFromStartTime"), eq("valFromEndTime"),
                eq("valToStartTime"), eq("valToEndTime"), eq(0), eq(0), any(Integer[].class),
                any(Integer[].class))).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .param("periodMin", "0")
                .param("periodMax", "0")
                .param("periodUnit", "periodUnit")
                .param("applyStartTime", "applyStartTime")
                .param("applyEndTime", "applyEndTime")
                .param("valFromStartTime", "valFromStartTime")
                .param("valFromEndTime", "valFromEndTime")
                .param("valToStartTime", "valToStartTime")
                .param("valToEndTime", "valToEndTime")
                .param("keyType", "0")
                .param("keyResource", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testSelectForPage_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPage(eq(0), eq(0), eq("userId"), eq("vin"), eq(0), eq(0), eq("periodUnit"),
                eq("applyStartTime"), eq("applyEndTime"), eq("valFromStartTime"), eq("valFromEndTime"),
                eq("valToStartTime"), eq("valToEndTime"), eq(0), eq(0), any(Integer[].class),
                any(Integer[].class))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .param("periodMin", "0")
                .param("periodMax", "0")
                .param("periodUnit", "periodUnit")
                .param("applyStartTime", "applyStartTime")
                .param("applyEndTime", "applyEndTime")
                .param("valFromStartTime", "valFromStartTime")
                .param("valFromEndTime", "valFromEndTime")
                .param("valToStartTime", "valToStartTime")
                .param("valToEndTime", "valToEndTime")
                .param("keyType", "0")
                .param("keyResource", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testSelectForPageByUserId() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByUserId(0, 0, 0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByUserId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPageByUserId_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByUserId(0, 0, 0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByUserId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectForPageByVehicleId() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByVehicleId(0, 0, 0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByVehicleId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vehicleId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPageByVehicleId_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByVehicleId(0, 0, 0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByVehicleId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vehicleId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testUpdateStateById() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.updateStateById("keyId", 0, "userId", "vin")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKey/updateStateById")
                .param("keyId", "keyId")
                .param("dkState", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testUpdateStateById_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.updateStateById("keyId", 0, "userId", "vin")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKey/updateStateById")
                .param("keyId", "keyId")
                .param("dkState", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testUpdateStateForRevokeById() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.updateStateForRevokeById("userId", "vin")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKey/updateStateForRevokeById")
                .param("userId", "userId")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testUpdateStateForRevokeById_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.updateStateForRevokeById("userId", "vin")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKey/updateStateForRevokeById")
                .param("userId", "userId")
                .param("vin", "vin")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectForPageByVal() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByVal(0, 0, "valFrom", "valTo", 0L, 0L))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByVal")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("valFrom", "valFrom")
                .param("valTo", "valTo")
                .param("period", "0")
                .param("dkState", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPageByVal_DkmKeyServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyServiceImpl.selectForPageByVal(0, 0, "valFrom", "valTo", 0L, 0L)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKey/selectForPageByVal")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("valFrom", "valFrom")
                .param("valTo", "valTo")
                .param("period", "0")
                .param("dkState", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testStartLoadKeyLogExcel() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportMapper.insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0))).thenReturn(0);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKey/downloadKeyExcel")
                .param("vin", "vin")
                .param("userId", "userId")
                .param("keyType", "0")
                .param("applyStartTime", "applyStartTime")
                .param("applyEndTime", "applyEndTime")
                .param("periodMax", "0")
                .param("periodMin", "0")
                .param("periodUnit", "periodUnit")
                .param("valFromStartTime", "valFromStartTime")
                .param("valFromEndTime", "valFromEndTime")
                .param("valToStartTime", "valToStartTime")
                .param("valToEndTime", "valToEndTime")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .param("keyResource", "0")
                .param("creator", "creator")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo("{\"code\":200,\"msg\":\"正在导出\"}");
        verify(mockDkmKeyLogHistoryExportMapper).insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0));
        verify(mockDkmKeyServiceImpl).downloadKeyLogExcel(eq("vin"), eq("userId"), eq(0), eq("applyStartTime"),
                eq("applyEndTime"), eq(0), eq(0), eq("periodUnit"), eq("valFromStartTime"), eq("valFromEndTime"),
                eq("valToStartTime"), eq("valToEndTime"), any(Integer[].class), eq(0), eq("creator"), eq("excelName"),
                any(Integer[].class));
    }
}
