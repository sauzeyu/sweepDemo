package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmStatisticsServiceImpl;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmStatisticsController.class)
class DkmStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmStatisticsServiceImpl mockEchartsServiceImpl;

    @Test
    void testSelectUserTotal() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectTotal(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmStatistics/selectTotal")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectUserTotal_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectTotal(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmStatistics/selectTotal")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectVehicleAndKeyAndKeyLogTotal() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectVehicleAndKeyAndKeyLogTotal()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectVehicleAndKeyAndKeyLogTotal")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectVehicleAndKeyAndKeyLogTotal_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectVehicleAndKeyAndKeyLogTotal()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectVehicleAndKeyAndKeyLogTotal")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyLogByMonth() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyLogByMonth()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyLogByMonth")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyLogByMonth_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyLogByMonth()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyLogByMonth")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyUseLogByTime() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyUseLogByTime("startTime", "endTime")).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyUseLogByTime")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyUseLogByTime_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyUseLogByTime("startTime", "endTime")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyUseLogByTime")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByTime() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByTime("startTime", "endTime"))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByTime")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByTime_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByTime("startTime", "endTime")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByTime")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByAllPhoneBrand() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByAllPhoneBrand()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByAllPhoneBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByAllPhoneBrand_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByAllPhoneBrand()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByAllPhoneBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByPhoneBrand() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByPhoneBrand("phoneBrand")).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByPhoneBrand")
                .param("phoneBrand", "phoneBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectKeyErrorLogByPhoneBrand_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectKeyErrorLogByPhoneBrand("phoneBrand")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectKeyErrorLogByPhoneBrand")
                .param("phoneBrand", "phoneBrand")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testVehicleStatistics() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.vehicleStatistics()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/vehicleStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testVehicleStatistics_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.vehicleStatistics()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/vehicleStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyStatistics() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyStatistics()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyStatistics_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyStatistics()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyUseTimeStatistics() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyUseTimeStatistics()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyUseTimeStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyUseTimeStatistics_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyUseTimeStatistics()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyUseTimeStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyErrorTimeStatistics() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyErrorTimeStatistics()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyErrorTimeStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testKeyErrorTimeStatistics_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.keyErrorTimeStatistics()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/keyErrorTimeStatistics")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectErrorStatusTotal() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectErrorStatusTotal(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectErrorStatusTotal")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectErrorStatusTotal_DkmStatisticsServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockEchartsServiceImpl.selectErrorStatusTotal(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmStatistics/selectErrorStatusTotal")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
