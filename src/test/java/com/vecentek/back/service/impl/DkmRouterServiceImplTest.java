package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmAdminRole;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.entity.DkmRoleMenu;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.mapper.DkmRoleMenuMapper;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DkmRouterServiceImplTest {

    @Mock
    private DkmAdminMapper mockDkmAdminMapper;
    @Mock
    private DkmAdminRoleMapper mockDkmAdminRoleMapper;
    @Mock
    private DkmRoleMenuMapper mockDkmRoleMenuMapper;
    @Mock
    private DkmMenuMapper mockDkmMenuMapper;

    @InjectMocks
    private DkmRouterServiceImpl dkmRouterServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmAdmin.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmRoleMenu.class);
    }
    @Test
    void testGetPageRouter() {
        // Setup
        final PageResp expectedResult = PageResp.success("获取成功");

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));
        when(mockDkmRoleMenuMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmRoleMenu(0, 0, 0)));

        // Configure DkmMenuMapper.selectList(...).
         List<DkmMenu> dkmMenus = new ArrayList<>();
        DkmMenu dkmMenu1 = new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna");
        DkmMenu dkmMenu = new DkmMenu(0, null, "title", "icon", "href", "target", "isShow", 0, "dna");
        dkmMenus.add(dkmMenu);
        dkmMenus.add(dkmMenu1);
        when(mockDkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmMenus);

        // Run the test
        final PageResp result = dkmRouterServiceImplUnderTest.getPageRouter(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetPageRouter_DkmAdminMapperReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("用户不存在！");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(null);

        // Run the test
        final PageResp result = dkmRouterServiceImplUnderTest.getPageRouter(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetPageRouter_DkmAdminRoleMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("用户对应角色不存在！");

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmRouterServiceImplUnderTest.getPageRouter(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetPageRouter_DkmRoleMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("获取成功");

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));
        when(mockDkmRoleMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmRouterServiceImplUnderTest.getPageRouter(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetPageRouter_DkmMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("获取成功");

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));
        when(mockDkmRoleMenuMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmRoleMenu(0, 0, 0)));
        when(mockDkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmRouterServiceImplUnderTest.getPageRouter(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
