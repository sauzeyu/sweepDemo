package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.service.impl.DkmKeyLogServiceImpl;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmKeyLogController.class)
class DkmKeyLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmKeyLogServiceImpl mockDkmKeyUseLogService;
    @MockBean
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmKeyUseLogService.selectForPage(0, 0, "vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType"))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKeyLog/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .param("phoneBrand", "phoneBrand")
                .param("phoneModel", "phoneModel")
                .param("statusCode", "statusCode")
                .param("flag", "0")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleType", "vehicleType")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testSelectForPage_DkmKeyLogServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyUseLogService.selectForPage(0, 0, "vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType"))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKeyLog/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("userId", "userId")
                .param("vin", "vin")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .param("phoneBrand", "phoneBrand")
                .param("phoneModel", "phoneModel")
                .param("statusCode", "statusCode")
                .param("flag", "0")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleType", "vehicleType")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testStartLoadKeyLogExcel() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportMapper.insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0))).thenReturn(0);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmKeyLog/downloadKeyLogExcel")
                .param("vin", "vin")
                .param("userId", "userId")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .param("phoneBrand", "phoneBrand")
                .param("phoneModel", "phoneModel")
                .param("statusCode", "statusCode")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleType", "vehicleType")
                .param("flag", "0")
                .param("creator", "creator")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo("{\"code\":200,\"msg\":\"正在导出\"}");
        verify(mockDkmKeyLogHistoryExportMapper).insert(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0));
        verify(mockDkmKeyUseLogService).downloadKeyLogExcel("vin", "userId", "startTime", "endTime", "phoneBrand",
                "phoneModel", Arrays.asList("value"), 0, "vehicleBrand", "vehicleModel", "vehicleType", "excelName");
    }
}
