package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.common.response.PageResp;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:10
 */
@SpringBootTest
public class DkmBluetoothsServiceImplTest {
    @MockBean
    private DkmBluetoothsMapper dkmBluetoothsMapper;

    @Autowired
    private DkmBluetoothsServiceImpl dkmBluetoothsService;

    @Test
    public void testSelectForPage() {
        // 调用接口
        PageResp pageResp = dkmBluetoothsService.selectForPage(1, 10, "hw123456", "search123", 1);
        // 断言
        Assert.assertEquals("查询成功", pageResp.getMsg());
    }

    @Test
    public void testDeleteById() {
        String hwDeviceSn = "hw123456";
        dkmBluetoothsService.deleteById(hwDeviceSn);
        // 验证是否成功删除
        Mockito.verify(dkmBluetoothsMapper, Mockito.times(1)).delete(Wrappers.<DkmBluetooths>lambdaQuery().eq(DkmBluetooths::getHwDeviceSn, hwDeviceSn));
    }

    @Test
    public void testDownloadDkmBluetooths() {
        // 创建假数据
        List<DkmBluetooths> mockList = new ArrayList<>();
        DkmBluetooths dkmBluetooths = new DkmBluetooths();
        dkmBluetooths.setHwDeviceSn("hw123456");
        mockList.add(dkmBluetooths);
        Mockito.when(dkmBluetoothsMapper.selectList(Mockito.any(LambdaQueryWrapper.class))).thenReturn(mockList);

        // 创建mock response
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // 调用接口
        dkmBluetoothsService.downloadDkmBluetooths("hw123456", "search123", 1, response);

        // 验证是否设置header和写入数据到response
        Mockito.verify(response, Mockito.times(1)).setHeader(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(response, Mockito.times(1)).setCharacterEncoding(Mockito.anyString());
        Mockito.verify(response, Mockito.times(1)).setContentType(Mockito.anyString());
        try {
            Mockito.verify(response.getOutputStream(), Mockito.times(1)).flush();
        } catch (IOException e) {
            e.printStackTrace(); // ignore
        }
    }
}
