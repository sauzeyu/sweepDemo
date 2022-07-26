package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.RoleMenuDTO;
import com.vecentek.back.entity.DkmRole;
import com.vecentek.back.entity.DkmRoleMenu;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.mapper.DkmRoleMapper;
import com.vecentek.back.mapper.DkmRoleMenuMapper;
import com.vecentek.back.vo.InsertRoleVO;
import com.vecentek.back.vo.RoleDTO;
import com.vecentek.common.response.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-17 15:39
 */
@Service
public class DkmRoleServiceImpl {


    @Resource
    private DkmRoleMapper dkmRoleMapper;
    @Resource
    private DkmMenuMapper dkmMenuMapper;
    @Resource
    private DkmRoleMenuMapper dkmRoleMenuMapper;
    @Resource
    private DkmAdminRoleMapper dkmAdminRoleMapper;


    public PageResp selectForPage(int pageIndex, int pageSize, String roleName, Integer code, String startTime, String endTime) {
        Page<DkmRole> page = new Page<>(pageIndex, pageSize);
        List<RoleMenuDTO> rolesMenu = new ArrayList<>();


        LambdaQueryWrapper<DkmRole> queryWrapper = Wrappers.<DkmRole>lambdaQuery()
                .eq(code != null, DkmRole::getCode, code)
                .like(StringUtils.isNotBlank(roleName), DkmRole::getRoleName, roleName)
                .ge(StringUtils.isNotBlank(startTime), DkmRole::getCreateTime, startTime)
                .le(StringUtils.isNotBlank(endTime), DkmRole::getCreateTime, endTime);

        Page<DkmRole> dkmRolePage = dkmRoleMapper.selectPage(page, queryWrapper);
        for (DkmRole role : dkmRolePage.getRecords()) {
            RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
            BeanUtils.copyProperties(role, roleMenuDTO);
            roleMenuDTO.setMenuIds(dkmMenuMapper.selectMenuByRoleId(role.getId()));
            rolesMenu.add(roleMenuDTO);

        }
        return PageResp.success("查询成功", page.getTotal(), rolesMenu);
    }


    @Transactional(rollbackFor = Exception.class)
    public PageResp deleteById(int id) {
        dkmRoleMapper.deleteById(id);
        dkmRoleMenuMapper.delete(Wrappers.<DkmRoleMenu>lambdaQuery().eq(DkmRoleMenu::getRoleId, id));
        dkmAdminRoleMapper.deleteByRoleId(id);
        return PageResp.success("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public PageResp updateRoleById(RoleDTO role) {
        DkmRole dkmRole = new DkmRole();
        BeanUtils.copyProperties(role, dkmRole);
        dkmRoleMapper.update(dkmRole, Wrappers.<DkmRole>lambdaUpdate().eq(DkmRole::getId, role.getId()));
        dkmRoleMenuMapper.delete(Wrappers.<DkmRoleMenu>lambdaQuery().eq(DkmRoleMenu::getRoleId, dkmRole.getId()));
        for (String menuId : role.getCheckedKey()) {
            DkmRoleMenu dkmRoleMenu = new DkmRoleMenu();
            dkmRoleMenu.setRoleId(role.getId());
            dkmRoleMenu.setMenuId(Integer.parseInt(menuId));
            dkmRoleMenuMapper.insert(dkmRoleMenu);
        }
        return PageResp.success("操作成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public PageResp insert(InsertRoleVO roleVO) {
        DkmRole role = new DkmRole();
        BeanUtils.copyProperties(roleVO, role);
        if (roleVO.getMenuList() == null || roleVO.getMenuList().size() == 0) {
            return PageResp.fail("菜单权限不能未空！");
        }
        if (StrUtil.isBlank(role.getRoleName()) || role.getCode() == null) {
            return PageResp.fail("角色名称或角色代码不能为空！");
        }
        DkmRole selectCode = dkmRoleMapper.selectOne(Wrappers.<DkmRole>lambdaQuery().eq(DkmRole::getCode, role.getCode()));
        DkmRole selectRoleName = dkmRoleMapper.selectOne(Wrappers.<DkmRole>lambdaQuery().eq(DkmRole::getRoleName, role.getRoleName()));
        if (selectCode != null || selectRoleName != null) {
            return PageResp.fail("角色名称或角色代码已存在！");
        }
        role.setCreateTime(new Date());
        dkmRoleMapper.insert(role);
        for (String menuId : roleVO.getMenuList()) {
            DkmRoleMenu dkmRoleMenu = new DkmRoleMenu();
            dkmRoleMenu.setRoleId(role.getId());
            dkmRoleMenu.setMenuId(Integer.parseInt(menuId));
            dkmRoleMenuMapper.insert(dkmRoleMenu);
        }
        return PageResp.success("新增成功");
    }
}
