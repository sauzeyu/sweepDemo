package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmDkmKeyLifecycleServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmKeyLifecycleController.class)
class DkmKeyLifecycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmDkmKeyLifecycleServiceImpl mockDkmKeyLifecycleService;

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmKeyLifecycleService.selectForPage(0, 0, "keyId")).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKeyLifecycle/selectForPageByKeyId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("keyId", "keyId")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectForPage_DkmDkmKeyLifecycleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmKeyLifecycleService.selectForPage(0, 0, "keyId")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmKeyLifecycle/selectForPageByKeyId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("keyId", "keyId")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
