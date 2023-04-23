package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.exception.ParameterValidationException;
import com.vecentek.back.exception.UploadOverMaximumException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.service.impl.DkmOfflineCheckServiceImpl;
import com.vecentek.back.vo.KeyLogDataVO;
import com.vecentek.back.vo.KeyLogDetailVO;
import com.vecentek.back.vo.VehicleBluetoothVO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DkmOfflineCheckController.class)
class DkmOfflineCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DkmOfflineCheckServiceImpl mockDkmOfflineCheckServiceImpl;
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Test
    void testInsertBluetoothBatch() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")))).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertBluetoothBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertBluetoothBatch_DkmOfflineCheckServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertBluetoothBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertBluetoothBatch_DkmOfflineCheckServiceImplThrowsParameterValidationException() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))))
                .thenThrow(ParameterValidationException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertBluetoothBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertBluetoothBatch_DkmOfflineCheckServiceImplThrowsUploadOverMaximumException() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))))
                .thenThrow(UploadOverMaximumException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertBluetoothBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertOrUpdateVehicleBatch() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertOrUpdateVehicleBatch(
                Arrays.asList(new VehicleBluetoothVO()))).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertOrUpdateVehicleBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmOfflineCheckServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertOrUpdateVehicleBatch(
                Arrays.asList(new VehicleBluetoothVO()))).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertOrUpdateVehicleBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmOfflineCheckServiceImplThrowsVecentException() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.insertOrUpdateVehicleBatch(
                Arrays.asList(new VehicleBluetoothVO()))).thenThrow(VecentException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/insertOrUpdateVehicleBatch")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testGetKeyLogDetail() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.getKeyLogDetail(new KeyLogDetailVO())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/getKeyLogDetail")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testGetKeyLogDetail_DkmOfflineCheckServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.getKeyLogDetail(new KeyLogDetailVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/getKeyLogDetail")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testGetKeyData() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.getKeyData(new KeyLogDataVO())).thenReturn(PageResp.success("msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/getKeyData")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }

    @Test
    void testGetKeyData_DkmOfflineCheckServiceImplReturnsFailure() throws Exception {
        // Setup
        when(mockDkmOfflineCheckServiceImpl.getKeyData(new KeyLogDataVO())).thenReturn(PageResp.fail());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/offlineCheck/getKeyData")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        String contentAsString = response.getContentAsString(Charset.defaultCharset());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(contentAsString).isEqualTo(oneThousandOneMessageResponse);
    }
}
