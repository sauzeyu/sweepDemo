package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vecentek.back.dto.ChildRouterDTO;
import com.vecentek.back.dto.ParentRouterDTO;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmAdminRole;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.entity.DkmRoleMenu;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.mapper.DkmRoleMenuMapper;
import com.vecentek.common.response.PageResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:36
 */
@RunWith(MockitoJUnitRunner.class)
public class DkmRouterServiceImplTest {
    @InjectMocks
    private DkmRouterServiceImpl dkmRouterService;

    @Mock
    private DkmAdminMapper dkmAdminMapper;

    @Mock
    private DkmAdminRoleMapper dkmAdminRoleMapper;

    @Mock
    private DkmRoleMenuMapper dkmRoleMenuMapper;

    @Mock
    private DkmMenuMapper dkmMenuMapper;

    private DkmAdmin dkmAdmin;
    private List<DkmAdminRole> dkmAdminRoles;
    private DkmAdminRole dkmAdminRole;
    private List<DkmRoleMenu> dkmRoleMenus;
    private DkmRoleMenu dkmRoleMenu;
    private List<DkmMenu> dkmMenus;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dkmAdmin = new DkmAdmin();
        dkmAdmin.setId(1);
        dkmAdmin.setUsername("admin");

        dkmAdminRoles = new ArrayList<>();
        dkmAdminRole = new DkmAdminRole();
        dkmAdminRole.setAdminId(1);
        dkmAdminRole.setRoleId(1);
        dkmAdminRoles.add(dkmAdminRole);

        dkmRoleMenus = new ArrayList<>();
        dkmRoleMenu = new DkmRoleMenu();
        dkmRoleMenu.setRoleId(1);
        dkmRoleMenu.setMenuId(1);
        dkmRoleMenus.add(dkmRoleMenu);

        dkmMenus = new ArrayList<>();
        DkmMenu parentMenu = new DkmMenu();
        parentMenu.setId(1);
        parentMenu.setTitle("Parent Menu");
        parentMenu.setHref("parent");
        dkmMenus.add(parentMenu);

        DkmMenu childMenu = new DkmMenu();
        childMenu.setId(2);
        childMenu.setTitle("Child Menu");
        childMenu.setParentId(1);
        childMenu.setHref("child");
        dkmMenus.add(childMenu);
    }

    @Test
    public void testGetPageRouterSuccess() {
        when(dkmAdminMapper.selectById(1)).thenReturn(dkmAdmin);
        when(dkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmAdminRoles);
        when(dkmRoleMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmRoleMenus);
        when(dkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmMenus);

        PageResp pageResp = dkmRouterService.getPageRouter(1);

        verify(dkmAdminMapper, times(1)).selectById(1);
        verify(dkmAdminRoleMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
        verify(dkmRoleMenuMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
        verify(dkmMenuMapper, times(1)).selectList(any(LambdaQueryWrapper.class));

        assertEquals("获取成功", pageResp.getMsg());

        List<ParentRouterDTO> parentRouterDTOS = (List<ParentRouterDTO>) pageResp.getData();
        assertEquals(1, parentRouterDTOS.size());

        ParentRouterDTO parentRouterDTO = parentRouterDTOS.get(0);
        assertEquals("/parent", parentRouterDTO.getPath());
        assertEquals("Parent Menu", parentRouterDTO.getTitle());

        List<ChildRouterDTO> childRouterDTOS = parentRouterDTO.getRoutes();
        assertEquals(1, childRouterDTOS.size());

        ChildRouterDTO childRouterDTO = childRouterDTOS.get(0);
        assertEquals("/parent/child", childRouterDTO.getPath());
        assertEquals("Child Menu", childRouterDTO.getTitle());
    }

    @Test
    public void testGetPageRouterFailUserNotExist() {
        when(dkmAdminMapper.selectById(2)).thenReturn(null);

        PageResp pageResp = dkmRouterService.getPageRouter(2);

        verify(dkmAdminMapper, times(1)).selectById(2);
        verify(dkmAdminRoleMapper, never()).selectList(any(LambdaQueryWrapper.class));
        verify(dkmRoleMenuMapper, never()).selectList(any(LambdaQueryWrapper.class));
        verify(dkmMenuMapper, never()).selectList(any(LambdaQueryWrapper.class));

        assertEquals("用户不存在！", pageResp.getMsg());
    }

    @Test
    public void testGetPageRouterFailUserRoleNotExist() {
        when(dkmAdminMapper.selectById(1)).thenReturn(dkmAdmin);
        when(dkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        PageResp pageResp = dkmRouterService.getPageRouter(1);

        verify(dkmAdminMapper, times(1)).selectById(1);
        verify(dkmAdminRoleMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
        verify(dkmRoleMenuMapper, never()).selectList(any(LambdaQueryWrapper.class));
        verify(dkmMenuMapper, never()).selectList(any(LambdaQueryWrapper.class));

        assertEquals("用户对应角色不存在！", pageResp.getMsg());
    }
}
