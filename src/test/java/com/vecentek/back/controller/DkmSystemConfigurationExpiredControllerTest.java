package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmSystemConfigurationExpired;
import com.vecentek.back.service.impl.DkmSystemConfigurationExpiredServiceImpl;
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
@WebMvcTest(DkmSystemConfigurationExpiredController.class)
class DkmSystemConfigurationExpiredControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmSystemConfigurationExpiredServiceImpl mockDkmSystemConfigurationExpiredService;

    @Test
    void testSelectForExpiration() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.selectForExpiration()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                get("/dkmSystemConfigurationExpired/selectForExpiration")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectForExpiration_DkmSystemConfigurationExpiredServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.selectForExpiration()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                get("/dkmSystemConfigurationExpired/selectForExpiration")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectForLast() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.selectForLast()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmSystemConfigurationExpired/selectForLast")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectForLast_DkmSystemConfigurationExpiredServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.selectForLast()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmSystemConfigurationExpired/selectForLast")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSaveOrUpdateConfigExpired() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(
                new DkmSystemConfigurationExpired(0, "username", 0,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmSystemConfigurationExpired/saveOrUpdateConfigExpired")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSaveOrUpdateConfigExpired_DkmSystemConfigurationExpiredServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(
                new DkmSystemConfigurationExpired(0, "username", 0,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                post("/dkmSystemConfigurationExpired/saveOrUpdateConfigExpired")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
