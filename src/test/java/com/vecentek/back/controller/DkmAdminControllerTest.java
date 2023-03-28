package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmAdminServiceImpl;
import com.vecentek.back.util.DownLoadUtil;
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
@WebMvcTest(DkmAdminController.class)
class DkmAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmAdminServiceImpl mockDkmAdminService;

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmAdminService.selectForPage(1, 10, "username", "startTime", "endTime"))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectForPage")
                .param("pageIndex", "1")
                .param("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testSelectForPage_DkmAdminServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmAdminService.selectForPage(0, 0, "username", "startTime", "endTime")).thenReturn(PageResp.fail());

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
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //assertThat(response.getContentAsString()).isEqualTo("{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}");
    }

    @Test
    void testSelectAdminInfoById() throws Exception {
        // Setup
        when(mockDkmAdminService.selectRoleNameListById(0)).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectRoleNameListById")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
    @Test
            void testDay() {
        String now = DownLoadUtil.getNow();
        String nextDay = DownLoadUtil.getNextDay();
        System.out.println("DkmAdminControllerTest.testDay");
    }

    //@Test
    //void testSelectAdminInfoById_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.selectRoleNameListById(0)).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectRoleNameListById")
    //            .param("id", "0")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testSelectAllRole() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.selectAllRole()).thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectAllRole")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testSelectAllRole_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.selectAllRole()).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(get("/dkmAdmin/selectAllRole")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testUpdateAdminById() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.updateAdminById(new AdminVO(0))).thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/updateAdminById")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testUpdateAdminById_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.updateAdminById(new AdminVO(0))).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/updateAdminById")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testResetPasswordById() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.resetPasswordById(new AdminVO(0))).thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/resetPasswordById")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testResetPasswordById_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.resetPasswordById(new AdminVO(0))).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/resetPasswordById")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testModifyPassword() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.modifyPassword("username", "password", "newPassword"))
    //            .thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/modifyPassword")
    //            .param("username", "username")
    //            .param("password", "password")
    //            .param("newPassword", "newPassword")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testModifyPassword_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.modifyPassword("username", "password", "newPassword")).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/modifyPassword")
    //            .param("username", "username")
    //            .param("password", "password")
    //            .param("newPassword", "newPassword")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testDeleteById() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.deleteById(0)).thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/deleteById")
    //            .param("id", "0")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testDeleteById_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.deleteById(0)).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/deleteById")
    //            .param("id", "0")
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testInsert() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.insert(new InsertAdminVO())).thenReturn(PageResp.success("msg"));
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/insert")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
    //
    //@Test
    //void testInsert_DkmAdminServiceImplReturnsFailure() throws Exception {
    //    // Setup
    //    when(mockDkmAdminService.insert(new InsertAdminVO())).thenReturn(PageResp.fail());
    //
    //    // Run the test
    //    final MockHttpServletResponse response = mockMvc.perform(post("/dkmAdmin/insert")
    //            .content("content").contentType(MediaType.APPLICATION_JSON)
    //            .accept(MediaType.APPLICATION_JSON))
    //            .andReturn().getResponse();
    //
    //    // Verify the results
    //    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    //    assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    //}
}
