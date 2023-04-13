package com.vecentek.back.service.impl;

import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmAdminRole;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.vo.AdminVO;
import com.vecentek.back.vo.InsertAdminVO;
import com.vecentek.common.response.PageResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DkmAdminServiceImplTest {

    @Mock
    private DkmAdminMapper dkmAdminMapper;

    @Mock
    private DkmAdminRoleMapper dkmAdminRoleMapper;

    @Mock
    private RedisTemplate redisTemplate;

    @InjectMocks
    private DkmAdminServiceImpl dkmAdminService;

    @Test
    public void testInsertSuccess() {
        InsertAdminVO adminVO = new InsertAdminVO();
        adminVO.setUsername("testuser");
        adminVO.setPassword("testpass");
        adminVO.setRoleId(1);

        when(dkmAdminMapper.selectOne(any())).thenReturn(null); // mock user not exist
        when(dkmAdminMapper.insert(any(DkmAdmin.class))).thenReturn(1); // mock insert success
        when(dkmAdminRoleMapper.insert(any(DkmAdminRole.class))).thenReturn(1); // mock insert success

        PageResp resp = dkmAdminService.insert(adminVO);

        assertEquals(200, resp.getCode());
        assertEquals("新增成功", resp.getMsg());

        verify(dkmAdminMapper, times(1)).selectOne(any());
        verify(dkmAdminMapper, times(1)).insert(any());
        verify(dkmAdminRoleMapper, times(1)).insert(any());
    }

    @Test
    public void testInsertUserAlreadyExist() {
        InsertAdminVO adminVO = new InsertAdminVO();
        adminVO.setUsername("testuser");
        adminVO.setPassword("testpass");
        adminVO.setRoleId(1);

        when(dkmAdminMapper.selectOne(any())).thenReturn(new DkmAdmin()); // mock user already exists

        PageResp resp = dkmAdminService.insert(adminVO);

        assertEquals(500, resp.getCode());
        assertEquals("该用户名已存在", resp.getMsg());

        verify(dkmAdminMapper, times(1)).selectOne(any());
        verify(dkmAdminMapper, times(0)).insert(any());
        verify(dkmAdminRoleMapper, times(0)).insert(any());
    }

    @Test
    public void testResetPasswordById() {
        Integer userId = 1;
        String newPassword = "newpass";
        AdminVO adminVO = new AdminVO();
        adminVO.setId(userId);
        adminVO.setPassword(newPassword);
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("testuser");

        DkmAdmin existingAdmin = new DkmAdmin();
        existingAdmin.setId(userId);

        when(dkmAdminMapper.selectById(userId)).thenReturn(existingAdmin);
        when(dkmAdminMapper.update(any(), any())).thenReturn(1);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(redisTemplate.delete(anyString())).thenReturn(true);

        PageResp resp = dkmAdminService.resetPasswordById(adminVO);

        assertEquals(200, resp.getCode());
        assertEquals("更新成功", resp.getMsg());

        verify(dkmAdminMapper, times(1)).selectById(userId);
        verify(dkmAdminMapper, times(1)).update(any(), any());
        verify(redisTemplate, times(1)).hasKey(anyString());
        verify(redisTemplate, times(1)).delete(anyString());
    }

    @Test
    public void testResetPasswordByIdUserNotFound() {
        Integer userId = 1;
        String newPassword = "newpass";
        AdminVO adminVO = new AdminVO();
        adminVO.setId(userId);
        adminVO.setPassword(newPassword);
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("testuser");

        when(dkmAdminMapper.selectById(userId)).thenReturn(null); // user not found

        PageResp resp = dkmAdminService.resetPasswordById(adminVO);

        assertEquals(500, resp.getCode());
        assertEquals("用户已不存在", resp.getMsg());

        verify(dkmAdminMapper, times(1)).selectById(userId);
        verify(dkmAdminMapper, times(0)).update(any(), any());
        verify(redisTemplate, times(0)).hasKey(anyString());
        verify(redisTemplate, times(0)).delete(anyString());
    }
}
