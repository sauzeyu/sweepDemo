package com.vecentek.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.service.DkmKeyLogHistoryExportService;
import com.vecentek.back.service.impl.DkmKeyLogHistoryExportServiceImpl;
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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmKeyLogHistoryExportController.class)
class DkmKeyLogHistoryExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmKeyLogHistoryExportServiceImpl mockDkmKeyLogHistorysExportService;
    @MockBean
    private DkmKeyLogHistoryExportService mockDkmKeyLogHistoryExportService;

    @Test
    void testSelectAll() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportService.page(any(Page.class), any(QueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("dkmKeyLogHistoryExport")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectOne() throws Exception {
        // Setup
        // Configure DkmKeyLogHistoryExportService.getById(...).
        final DkmKeyLogHistoryExport dkmKeyLogHistoryExport = new DkmKeyLogHistoryExport(0, "missionName",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0);
        when(mockDkmKeyLogHistoryExportService.getById(any(Serializable.class))).thenReturn(dkmKeyLogHistoryExport);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("dkmKeyLogHistoryExport/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testInsert() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportService.save(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0))).thenReturn(false);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("dkmKeyLogHistoryExport")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdate() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportService.updateById(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0))).thenReturn(false);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("dkmKeyLogHistoryExport")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testDelete() throws Exception {
        // Setup
        when(mockDkmKeyLogHistoryExportService.removeByIds(Arrays.asList("value"))).thenReturn(false);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("dkmKeyLogHistoryExport")
                .param("idList", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testCheckKeyUseLog() throws Exception {
        // Setup
        when(mockDkmKeyLogHistorysExportService.checkKeyUseLog("creator", 0)).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("dkmKeyLogHistoryExport/checkKeyUseLog")
                .param("creator", "creator")
                .param("type", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testCheckKeyUseLog_DkmKeyLogHistoryExportServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyLogHistorysExportService.checkKeyUseLog("creator", 0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("dkmKeyLogHistoryExport/checkKeyUseLog")
                .param("creator", "creator")
                .param("type", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testDownloadExcel() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("dkmKeyLogHistoryExport/downloadExcel")
                .param("fileName", "fileName")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockDkmKeyLogHistorysExportService).downloadExcel(eq("fileName"), any(HttpServletResponse.class));
    }
}
