package com.vecentek.back.service.impl;

import com.vecentek.back.dto.TreeMenuDTO;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-18 17:02
 */
@Service
public class DkmMenuServiceImpl {
    @Resource
    private DkmMenuMapper dkmMenuMapper;


    private TreeMenuDTO castMenuToTreeNode(DkmMenu menu) {
        TreeMenuDTO treeNode = new TreeMenuDTO();
        treeNode.setKey(menu.getId().toString());
        treeNode.setTitle(menu.getTitle());
        treeNode.setParentId(menu.getParentId());
        return treeNode;
    }

    public PageResp selectAll() {
        List<DkmMenu> dkmMenus = dkmMenuMapper.selectList(null);
        List<TreeMenuDTO> parentMenuList = new ArrayList<>();
        List<TreeMenuDTO> subMenuList = new ArrayList<>();
        List<DkmMenu> parentMenu = dkmMenus.stream().filter(menu -> menu.getParentId() == null).collect(Collectors.toList());
        List<DkmMenu> subMenu = dkmMenus.stream().filter(menu -> menu.getParentId() != null).collect(Collectors.toList());

        for (DkmMenu menu : subMenu) {
            TreeMenuDTO subTreeNode = castMenuToTreeNode(menu);
            subMenuList.add(subTreeNode);
        }

        for (DkmMenu menu : parentMenu) {
            TreeMenuDTO parentTreeNode = castMenuToTreeNode(menu);
            parentMenuList.add(parentTreeNode);
        }
        for (TreeMenuDTO parentTree : parentMenuList) {
            parentTree.setChildren(new ArrayList<>());
            for (TreeMenuDTO subTree : subMenuList) {
                if (Objects.equals(subTree.getParentId().toString(), parentTree.getKey())) {
                    parentTree.getChildren().add(subTree);
                }

            }
        }
        return PageResp.success("查询成功", parentMenuList);
    }

    public PageResp selectMenuByRoleId(Integer id) {
        List<String> menuIds = dkmMenuMapper.selectMenuByRoleId(id);
        return PageResp.success("查询成功", menuIds);

    }
}
