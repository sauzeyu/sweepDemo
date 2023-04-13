package com.vecentek.back.service.impl;

import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.common.response.PageResp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class DkmUserServiceImplTest {

    @InjectMocks
    private DkmUserServiceImpl dkmUserService;

    @Mock
    private DkmUserMapper dkmUserMapper;

    private DkmUser dkmUser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dkmUser = new DkmUser();
        dkmUser.setId(String.valueOf(1));
        dkmUser.setUsername("test");
        dkmUser.setPhone("18888888888");
        when(dkmUserMapper.selectById(anyInt()))
                .thenReturn(dkmUser);
    }

    @Test
    public void selectByIdTest() {
        int id = 1;
        PageResp result = dkmUserService.selectById(id);
        Assert.assertEquals(java.util.Optional.ofNullable(result.getCode()), 0);
        Assert.assertEquals(result.getData(), dkmUser);
        Assert.assertEquals(result.getMessage(), "查询成功");
    }

    @Test
    public void selectByIdTestInvalid() {
        int id = 2;
        PageResp result = dkmUserService.selectById(id);
        Assert.assertEquals(result.getCode(), -1);
        Assert.assertEquals(result.getData(), null);
        Assert.assertEquals(result.getMessage(), "查询失败，用户不存在");
    }
}
