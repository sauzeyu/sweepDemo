package com.vecentek.back.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.TreeMenuDTO;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.common.response.PageResp;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:31
 */
@RunWith(MockitoJUnitRunner.class)
class DkmMenuServiceImplTest {

    @InjectMocks
    private DkmMenuServiceImpl dkmMenuService;

    @Mock
    private DkmMenuMapper dkmMenuMapper;

    @Test
    void selectAll() {
        // 准备数据
        List<DkmMenu> menuList = new ArrayList<>();
        DkmMenu menu1 = new DkmMenu();
        menu1.setId(1);
        menu1.setTitle("test1");
        menu1.setParentId(null);
        menuList.add(menu1);

        DkmMenu menu2 = new DkmMenu();
        menu2.setId(2);
        menu2.setTitle("test2");
        menu2.setParentId(1L);
        menuList.add(menu2);

        DkmMenu menu3 = new DkmMenu();
        menu3.setId(3L);
        menu3.setTitle("test3");
        menu3.setParentId(2L);
        menuList.add(menu3);

        List<TreeMenuDTO> treeMenuDTOList = new ArrayList<>();
        TreeMenuDTO treeMenuDTO1 = new TreeMenuDTO();
        treeMenuDTO1.setKey("1");
        treeMenuDTO1.setTitle("test1");
        treeMenuDTO1.setParentId(null);
        treeMenuDTO1.setChildren(new ArrayList<>());
        treeMenuDTOList.add(treeMenuDTO1);

        TreeMenuDTO treeMenuDTO2 = new TreeMenuDTO();
        treeMenuDTO2.setKey("2");
        treeMenuDTO2.setTitle("test2");
        treeMenuDTO2.setParentId(1L);
        treeMenuDTO2.setChildren(new ArrayList<>());
        treeMenuDTOList.add(treeMenuDTO2);

        TreeMenuDTO treeMenuDTO3 = new TreeMenuDTO();
        treeMenuDTO3.setKey("3");
        treeMenuDTO3.setTitle("test3");
        treeMenuDTO3.setParentId(2L);
        treeMenuDTO3.setChildren(new ArrayList<>());
        treeMenuDTOList.add(treeMenuDTO3);

        // 模拟方法调用
        when(dkmMenuMapper.selectList(null)).thenReturn(menuList);

        // 执行方法
        PageResp pageResp = dkmMenuService.selectAll();

        // 验证结果
        assertEquals(pageResp.getCode(), 0);
        assertEquals(pageResp.getMsg(), "查询成功");
        assertEquals(pageResp.getData(), treeMenuDTOList);
    }

    @Test
    void selectMenuByRoleId() {
        // 准备数据
        Integer id = 1;
        List<String> menuIds = new ArrayList<>();
        menuIds.add("1");
        menuIds.add("2");
        menuIds.add("3");

        // 模拟方法调用
        when(dkmMenuMapper.selectMenuByRoleId(id)).thenReturn(menuIds);

        // 执行方法
        PageResp pageResp = dkmMenuService.selectMenuByRoleId(id);

        // 验证结果
        assertEquals(pageResp.getCode(), 0);
        assertEquals(pageResp.getMsg(), "查询成功");
        assertEquals(pageResp.getData(), menuIds);
    }

    @Test
    void selectForPage() {
        // 准备数据
        Integer pageIndex = 1;
        Integer pageSize = 10;
        String title = "test";
        String icon = "icon";
        String href = "href";

        List<DkmMenu> menuList = new ArrayList<>();
        DkmMenu dkmMenu = new DkmMenu();
        dkmMenu.setId(1L);
        dkmMenu.setTitle("test");
        dkmMenu.setDna(1);
        dkmMenu.setIcon("icon");
        dkmMenu.setType(0);
        dkmMenu.setParentId(1L);
        dkmMenu.setHref("href");
        menuList.add(dkmMenu);

        Page<DkmMenu> page = new Page<>(pageIndex, pageSize);
        page.setRecords(menuList);
        page.setTotal(1);

        // 模拟方法调用
        when(dkmMenuMapper.selectPage(eq(page), any(LambdaQueryWrapper.class))).thenReturn(page);

        // 执行方法
        PageResp pageResp = dkmMenuService.selectForPage(pageIndex, pageSize, title, icon, href);

        // 验证结果
        assertEquals(pageResp.getCode(), 0);
        assertEquals(pageResp.getMsg(), "查询成功");
        assertEquals(pageResp.getData(), menuList);
        assertEquals(pageResp.getTotal(), 1);
    }

    @Test
    void selectByParentId() {
        // 准备数据
        Integer parentId = 1;

        List<DkmMenu> menuList = new ArrayList<>();
        DkmMenu dkmMenu = new DkmMenu();
        dkmMenu.setId(1L);
        dkmMenu.setTitle("test");
        dkmMenu.setDna(1);
        dkmMenu.setIcon("icon");
        dkmMenu.setType(0);
        dkmMenu.setParentId(1L);
        dkmMenu.setHref("href");
        menuList.add(dkmMenu);

        // 模拟方法调用
        when(dkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menuList);

        // 执行方法
        PageResp pageResp = dkmMenuService.selectByParentId(parentId);

        // 验证结果
        assertEquals(pageResp.getCode(), 0);
        assertEquals(pageResp.getMsg(), "查询成功");
        assertEquals(pageResp.getData(), menuList);
    }
}