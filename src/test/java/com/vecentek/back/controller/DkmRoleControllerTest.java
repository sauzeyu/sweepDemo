package com.vecentek.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.service.impl.DkmRoleServiceImpl;
import com.vecentek.back.vo.InsertRoleVO;
import com.vecentek.back.vo.RoleDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmRoleController.class)
class DkmRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmRoleServiceImpl mockDkmRoleService;
    @MockBean
    private DkmMenuMapper mockDkmMenuMapper;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmRoleService.selectForPage(0, 0, "roleName", "code", "startTime", "endTime"))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmRole/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("roleName", "roleName")
                .param("code", "code")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmRoleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmRoleService.selectForPage(0, 0, "roleName", "code", "startTime", "endTime"))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmRole/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("roleName", "roleName")
                .param("code", "code")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testUpdateRoleById() throws Exception {
        // Setup
        // Configure DkmMenuMapper.selectOne(...).
        final DkmMenu dkmMenu = new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna");
        when(mockDkmMenuMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmMenu);

        when(mockDkmRoleService.updateRoleById(new RoleDTO(Arrays.asList("value"))))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/updateRoleById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testUpdateRoleById_DkmRoleServiceImplReturnsFailure() throws Exception {
        // Setup
        // Configure DkmMenuMapper.selectOne(...).
        final DkmMenu dkmMenu = new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna");
        when(mockDkmMenuMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmMenu);

        when(mockDkmRoleService.updateRoleById(new RoleDTO(Arrays.asList("value")))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/updateRoleById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testDeleteById() throws Exception {
        // Setup
        when(mockDkmRoleService.deleteById(0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/deleteById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testDeleteById_DkmRoleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmRoleService.deleteById(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/deleteById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testInsert() throws Exception {
        // Setup
        // Configure DkmMenuMapper.selectOne(...).
        final DkmMenu dkmMenu = new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna");
        when(mockDkmMenuMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmMenu);

        when(mockDkmRoleService.insert(new InsertRoleVO())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/insert")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsert_DkmRoleServiceImplReturnsFailure() throws Exception {
        // Setup
        // Configure DkmMenuMapper.selectOne(...).
        final DkmMenu dkmMenu = new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna");
        when(mockDkmMenuMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmMenu);

        when(mockDkmRoleService.insert(new InsertRoleVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmRole/insert")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }
}
