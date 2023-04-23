package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.service.impl.DkmVehicleServiceImpl;
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
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmVehicleController.class)
class DkmVehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmVehicleServiceImpl mockDkmVehicleServiceImpl;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testSelectById() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectById(0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectById_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectById(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectById")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectUserByVehicleId() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectUserByVehicleId(0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectUserByVehicleId")
                .param("vehicleId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectUserByVehicleId_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectUserByVehicleId(0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectUserByVehicleId")
                .param("vehicleId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectForPage() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectForPage(0, 0, "vin", "hwDeviceSn", "vehicleModel", "vehicleBrand",
                "vehicleType")).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vin", "vin")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleType", "vehicleType")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPage_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectForPage(0, 0, "vin", "hwDeviceSn", "vehicleModel", "vehicleBrand",
                "vehicleType")).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectForPage")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("vin", "vin")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleType", "vehicleType")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(fiveHundredResponse);
    }

    @Test
    void testSelectForPageByUserId() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectForPageByUserId(0, 0, 0)).thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectForPageByUserId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
                .param("id", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(successResponse);
    }

    @Test
    void testSelectForPageByUserId_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.selectForPageByUserId(0, 0, 0)).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/dkmVehicle/selectForPageByUserId")
                .param("pageIndex", "0")
                .param("pageSize", "0")
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
        when(mockDkmVehicleServiceImpl.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicle/insert")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsert_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicle/insert")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testUpdateById() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.updateById(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")))
                .thenReturn(PageResp.success());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicle/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testUpdateById_DkmVehicleServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmVehicleServiceImpl.updateById(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")))
                .thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicle/updateById")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testDownloadDkmVehicle() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/dkmVehicle/downloadDkmVehicle")
                .param("vin", "vin")
                .param("hwDeviceSn", "hwDeviceSn")
                .param("vehicleModel", "vehicleModel")
                .param("vehicleBrand", "vehicleBrand")
                .param("vehicleType", "vehicleType")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(Charset.defaultCharset())).isEqualTo("");
        verify(mockDkmVehicleServiceImpl).downloadDkmVehicle(eq("vin"), eq("hwDeviceSn"), eq("vehicleModel"),
                eq("vehicleBrand"), eq("vehicleType"), any(HttpServletResponse.class));
    }
}
