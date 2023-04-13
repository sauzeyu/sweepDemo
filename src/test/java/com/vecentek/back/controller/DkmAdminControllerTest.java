package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmAdminServiceImpl;
import com.vecentek.back.vo.AdminVO;
import com.vecentek.back.vo.InsertAdminVO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmAdminController.class)
class DkmAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmAdminServiceImpl mockDkmAdminService;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmAdminService.selectForPage(1, 10, null, null, null))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectForPage")
                .param("pageIndex", "1")
                .param("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.selectForPage(1, 10, "err_name", "2023-01-02", "2022-01-02")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectForPage")
                .param("pageIndex", "")
                .param("pageSize", "")
                .param("username", "username")
                .param("startTime", "startTime")
                .param("endTime", "endTime")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(fiveHundredMessageResponse);
    }

    @Test
    void testSelectAdminInfoById() throws Exception {
        // Setup
        when(mockDkmAdminService.selectRoleNameListById(1)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectRoleNameListById")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testSelectAdminInfoById_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.selectRoleNameListById(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectRoleNameListById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectAllRole() throws Exception {
        // Setup
        when(mockDkmAdminService.selectAllRole()).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectAllRole")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testSelectAllRole_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.selectAllRole()).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectAllRole")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testUpdateAdminById() throws Exception {
        // Setup
        when(mockDkmAdminService.updateAdminById(new AdminVO(5))).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/updateAdminById")
                .content("{\"roleId\": \"5\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testUpdateAdminById_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.updateAdminById(new AdminVO(5))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/updateAdminById")
                .content("{\"roleId\": \"2sad213d\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testResetPasswordById() throws Exception {
        // Setup
        when(mockDkmAdminService.resetPasswordById(new AdminVO(5))).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/resetPasswordById")
                .content("{\"roleId\": \"5\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testResetPasswordById_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.resetPasswordById(new AdminVO(123))).thenReturn(PageResp.fail("用户已不存在"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/resetPasswordById")
                .content("{\"roleId\": \"123\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo("{\"code\":500,\"msg\":\"用户已不存在\"}");
    }

    @Test
    void testModifyPassword() throws Exception {
        // Setup
        when(mockDkmAdminService.modifyPassword("liujz", "202cb962ac59075b964b07152d234b70", "123"))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/modifyPassword")
                .param("username", "liujz")
                .param("password", "202cb962ac59075b964b07152d234b70")
                .param("newPassword", "123")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString =  response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testModifyPassword_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.modifyPassword("liujz", "123", "123")).thenReturn(PageResp.fail("密码错误，修改密码失败"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/modifyPassword")
                .param("username", "liujz")
                .param("password", "123")
                .param("newPassword", "123")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo("{\"code\":500,\"msg\":\"密码错误，修改密码失败\"}");
    }


    @Test
    void testDeleteById() throws Exception {
        // Setup
        when(mockDkmAdminService.deleteById(1)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/deleteById")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString =  response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testDeleteById_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.deleteById(0)).thenReturn(PageResp.fail("用户id不能为空"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/deleteById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo("{\"code\":500,\"msg\":\"用户id不能为空\"}");
    }

    @Test
    void testInsert() throws Exception {
        // Setup
        InsertAdminVO insertAdminVO = new InsertAdminVO();
        insertAdminVO.setRoleId(4);
        when(mockDkmAdminService.insert(insertAdminVO)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/insert")
                .content("{\"roleId\": \"4\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(successResponse);
    }

    @Test
    void testInsert_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.insert(new InsertAdminVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/insert")
                .content("{\"roleId\": \"\"}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString =  response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(fiveHundredResponse);
    }
}
