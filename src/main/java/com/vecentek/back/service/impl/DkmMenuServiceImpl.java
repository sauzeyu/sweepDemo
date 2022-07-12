package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    private List<TreeMenuDTO> generateTreeData(List<TreeMenuDTO> allNode) {
        return allNode.stream()
                .filter(node -> node.getParentId() == null)
                .peek(node -> node.setChildren(getChildList(node, allNode)))
                .collect(Collectors.toList());
    }

    private List<TreeMenuDTO> getChildList(TreeMenuDTO supperNode, List<TreeMenuDTO> allNode) {
        return allNode.stream()
                .filter(node -> Objects.equals(Integer.parseInt(supperNode.getKey()), node.getParentId()))
                .peek(node -> node.setChildren(getChildList(node, allNode)))
                .collect(Collectors.toList());
    }


    public PageResp selectAll() {


        List<DkmMenu> dkmMenus = dkmMenuMapper.selectList(null);
        List<TreeMenuDTO> treeMenuList = new ArrayList<>();
        dkmMenus.forEach(menu -> treeMenuList.add(castMenuToTreeNode(menu)));
        return PageResp.success("查询成功", generateTreeData(treeMenuList));
    }

    public PageResp selectMenuByRoleId(Integer id) {
        List<String> menuIds = dkmMenuMapper.selectMenuByRoleId(id);
        return PageResp.success("查询成功", menuIds);

    }

    public PageResp selectForPage(Integer pageIndex, Integer pageSize, String title, String icon, String href) {
        LambdaQueryWrapper<DkmMenu> lambdaQuery = Wrappers.lambdaQuery();
        Page<DkmMenu> page = new Page<>(pageIndex, pageSize);
        page = dkmMenuMapper.selectPage(page, lambdaQuery
                .like(StrUtil.isNotBlank(title), DkmMenu::getTitle, title)
                .like(StrUtil.isNotBlank(icon), DkmMenu::getIcon, icon)
                .like(StrUtil.isNotBlank(href), DkmMenu::getHref, href)
                .orderByAsc(DkmMenu::getDna)
        );

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }
}
