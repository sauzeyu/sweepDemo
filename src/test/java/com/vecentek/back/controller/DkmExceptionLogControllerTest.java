package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmFunctionalAbnormalServiceImpl;
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
@WebMvcTest(DkmExceptionLogController.class)
class DkmExceptionLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmFunctionalAbnormalServiceImpl mockDkmFunctionalAbnormalService;

    @Test
    void testSelectBusiness() throws Exception {
        // Setup
        when(mockDkmFunctionalAbnormalService.selectBusiness()).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmlog/selectBusiness")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectBusiness_DkmFunctionalAbnormalServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmFunctionalAbnormalService.selectBusiness()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmlog/selectBusiness")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
