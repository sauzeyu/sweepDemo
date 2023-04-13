package com.vecentek.back.service.impl;

import com.vecentek.back.mapper.DkmFunctionalAbnormalMapper;
import com.vecentek.common.response.PageResp;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:19
 */
@SpringBootTest
public class DkmFunctionalAbnormalServiceImplTest {
    @MockBean
    private DkmFunctionalAbnormalMapper dkmFunctionalAbnormalMapper;

    @Autowired
    private DkmFunctionalAbnormalServiceImpl dkmFunctionalAbnormalService;

    @Test
    public void testSelectBusiness() {
        // 创建假数据
        List<Map> mockList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "test");
        mockList.add(map);
        // 设置模拟行为
        Mockito.when(dkmFunctionalAbnormalMapper.selectBusiness()).thenReturn(mockList);
        // 调用接口
        PageResp pageResp = dkmFunctionalAbnormalService.selectBusiness();
        List<Map> dkmFunctionalAbnormal = (List<Map>) pageResp.getData();
        // 断言
        Assert.assertEquals("查询成功", pageResp.getMsg());
        Assert.assertEquals(1, dkmFunctionalAbnormal.size());
        Assert.assertEquals(1L, ((Map) dkmFunctionalAbnormal.get(0)).get("id"));
        Assert.assertEquals("test", ((Map) dkmFunctionalAbnormal.get(0)).get("name"));
    }
}
