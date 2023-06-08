package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmMenuServiceImpl;
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
@WebMvcTest(DkmMenuController.class)
class DkmMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmMenuServiceImpl mockDkmMenuService;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectAll() throws Exception {
        // Setup
        when(mockDkmMenuService.selectAll()).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectAll")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectAll_DkmMenuServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmMenuService.selectAll()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectAll")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmMenuService.selectForPage(0, 0, "title", "icon", "href")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("title", "title")
                .param("icon", "icon")
                .param("href", "href")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmMenuServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmMenuService.selectForPage(0, 0, "title", "icon", "href")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("title", "title")
                .param("icon", "icon")
                .param("href", "href")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectMenuByRoleId() throws Exception {
        // Setup
        when(mockDkmMenuService.selectMenuByRoleId(0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectMenuByRoleId")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectMenuByRoleId_DkmMenuServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmMenuService.selectMenuByRoleId(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectMenuByRoleId")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectByParentId() throws Exception {
        // Setup
        when(mockDkmMenuService.selectByParentId(0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectByParentId")
                .param("parentId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(successResponse);
    }

    @Test
    void testSelectByParentId_DkmMenuServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmMenuService.selectByParentId(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmMenu/selectByParentId")
                .param("parentId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(fiveHundredResponse);
    }
}
