package com.vecentek.back.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class DkmRouterServiceImpl {
    @Resource
    private DkmAdminMapper dkmAdminMapper;
    @Resource
    private DkmAdminRoleMapper dkmAdminRoleMapper;
    @Resource
    private DkmRoleMenuMapper dkmRoleMenuMapper;
    @Resource
    private DkmMenuMapper dkmMenuMapper;

    /**
     * 获取菜单路由，只包括一级二级路由
     *
     * @param id
     * @return
     */
    public PageResp getPageRouter(Integer id) {
        DkmAdmin dkmAdmin = dkmAdminMapper.selectById(id);
        if (dkmAdmin == null) {
            return PageResp.fail("用户不存在！");
        }
        List<DkmAdminRole> dkmAdminRoles = dkmAdminRoleMapper.selectList(new LambdaQueryWrapper<DkmAdminRole>().eq(DkmAdminRole::getAdminId, id));
        if (dkmAdminRoles.size() == 0) {
            return PageResp.fail("用户对应角色不存在！");
        }
        HashSet<DkmMenu> allMenu = new HashSet<>(); // Set去重
        for (DkmAdminRole dkmAdminRole : dkmAdminRoles) {
            Integer roleId = dkmAdminRole.getRoleId();
            List<DkmRoleMenu> dkmRoleMenus = dkmRoleMenuMapper.selectList(new LambdaQueryWrapper<DkmRoleMenu>().eq(DkmRoleMenu::getRoleId, roleId));
            for (DkmRoleMenu dkmRoleMenu : dkmRoleMenus) {
                List<DkmMenu> dkmMenus = dkmMenuMapper.selectList(new LambdaQueryWrapper<DkmMenu>()
                        .eq(DkmMenu::getId, dkmRoleMenu.getMenuId())
                        .ne(DkmMenu::getType, 1));// 不为按钮类型的权限
                if (CollUtil.isNotEmpty(dkmMenus)) {
                    allMenu.addAll(dkmMenus);
                }
            }

        }
        // 封装返回结果
        // 分为父和子 两个list分别组合
        ArrayList<DkmMenu> ParentList = new ArrayList<>();
        ArrayList<DkmMenu> ChildList = new ArrayList<>();
        for (DkmMenu menu : allMenu) {
            if (menu.getParentId() == null) {
                ParentList.add(menu);
            } else {
                ChildList.add(menu);
            }
        }
        ArrayList<ParentRouterDTO> res = new ArrayList<>();
        for (DkmMenu parent : ParentList) {
            ArrayList<ChildRouterDTO> routes = new ArrayList<>();
            for (DkmMenu child : ChildList) {
                if (Objects.equals(child.getParentId(), parent.getId())) {
                    ChildRouterDTO childRouterDTO = new ChildRouterDTO();
                    childRouterDTO.setPath("/" + parent.getHref() + "/" + child.getHref());
                    childRouterDTO.setTitle(child.getTitle());
                    routes.add(childRouterDTO);
                }
            }
            ParentRouterDTO parentRouterDTO = new ParentRouterDTO();
            parentRouterDTO.setPath("/" + parent.getHref());
            parentRouterDTO.setTitle(parent.getTitle());
            parentRouterDTO.setRoutes(routes);
            res.add(parentRouterDTO);
        }
        return PageResp.success("获取成功", res);
    }
}
