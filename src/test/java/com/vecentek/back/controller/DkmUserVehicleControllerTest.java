package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmUserVehicleServiceImpl;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
import com.vecentek.back.vo.UserVehicleVO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmUserVehicleController.class)
class DkmUserVehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmUserVehicleServiceImpl mockDkmUserVehicleService;

    @Test
    void testInsertUserVehicle() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.insertUserVehicle(
                new UserVehicleVO("username", "userId", "license", "vehicleType", "vin",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/insertUserVehicle")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testInsertUserVehicle_DkmUserVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.insertUserVehicle(
                new UserVehicleVO("username", "userId", "license", "vehicleType", "vin",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/insertUserVehicle")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testLogoutUserVehicle() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.logoutUserVehicle(new LogoutUserVehicleVO()))
                .thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/logoutUserVehicle")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testLogoutUserVehicle_DkmUserVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.logoutUserVehicle(new LogoutUserVehicleVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/logoutUserVehicle")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetBluetoothVin() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.getBluetoothVin(new GetBluetoothVinVO())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/getBluetoothVin")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetBluetoothVin_DkmUserVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.getBluetoothVin(new GetBluetoothVinVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/getBluetoothVin")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testRevokeKey() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.revokeKey(new RevokeKeyVO())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/revokeKey")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testRevokeKey_DkmUserVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmUserVehicleService.revokeKey(new RevokeKeyVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/userVehicle/revokeKey")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
