package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmBluetoothsServiceImplTest {

    @Mock
    private DkmBluetoothsMapper mockDkmBluetoothsMapper;
@Mock
private HttpServletResponse mockHttpServletResponse;
    @InjectMocks
    private DkmBluetoothsServiceImpl dkmBluetoothsServiceImplUnderTest;
    @MockBean
    private DkmBluetoothsServiceImpl mockdkmBluetoothsServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmBluetooths.class);
    }
    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmBluetoothsMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page());

        // Run the test
        final PageResp result = dkmBluetoothsServiceImplUnderTest.selectForPage(0, 0, "hwDeviceSn", "searchNumber", 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDeleteById() {
        // Setup
        final PageResp expectedResult = PageResp.success("删除成功");
        when(mockDkmBluetoothsMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Run the test
        final PageResp result = dkmBluetoothsServiceImplUnderTest.deleteById("hwDeviceSn");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmBluetoothsMapper).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    void testDownloadDkmBluetooths() {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        // Run the test
        dkmBluetoothsServiceImplUnderTest.downloadDkmBluetooths("hwDeviceSn", "searchNumber", 0, response);
        final List<DkmBluetooths> dkmBluetooths1 = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 1, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths1);
        dkmBluetoothsServiceImplUnderTest.downloadDkmBluetooths("hwDeviceSn", "searchNumber", 0, response);

        // Verify the results


    }
    @Test
    void testDownloadDkmBluetoothsThrowUnsupportedEncodingException() {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();
        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        doThrow(UnsupportedEncodingException.class).when(
                response).setHeader(any(), any());
        dkmBluetoothsServiceImplUnderTest.downloadDkmBluetooths("hwDeviceSn", "searchNumber", 0, response);


    }

}
