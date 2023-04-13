package com.vecentek.back.service.impl;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.common.response.PageResp;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:12
 */
@SpringBootTest
public class DkmDkmKeyLifecycleServiceImplTest {
    @MockBean
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;

    @Autowired
    private DkmDkmKeyLifecycleServiceImpl dkmDkmKeyLifecycleService;

    @Test
    @Ignore
    public void testSelectForPage() {
        // 创建假数据
        List<DkmKeyLifecycle> mockList = new ArrayList<>();
        DkmKeyLifecycle lifecycle = new DkmKeyLifecycle();
        lifecycle.setId(1L);
        lifecycle.setKeyId("key001");
        mockList.add(lifecycle);
        // 设置模拟行为
        //Mockito.when(dkmKeyLifecycleMapper.selectPage(Mockito.any(Page.class), Mockito.any(LambdaQueryWrapper.class))).thenReturn(new Page<DkmKeyLifecycle>(mockList));
        // 调用接口
        PageResp pageResp = dkmDkmKeyLifecycleService.selectForPage(1, 10, "key001");
        // 断言
        List<DkmKeyLifecycle> data = (List) pageResp.getData();
        Assert.assertEquals("查询成功", pageResp.getMsg());
        Assert.assertEquals(java.util.Optional.of(1), pageResp.getTotal());
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("key001", data.get(0).getKeyId());
    }
}
