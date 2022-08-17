package com.vecentek.back.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmAdminRole;
import com.vecentek.back.entity.DkmRole;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmRoleMapper;
import com.vecentek.back.vo.AdminVO;
import com.vecentek.back.vo.InsertAdminVO;
import com.vecentek.common.response.PageResp;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:38
 */
@Service
public class DkmAdminServiceImpl {

    @Resource
    private DkmAdminMapper dkmAdminMapper;

    @Resource
    private DkmRoleMapper dkmRoleMapper;

    @Resource
    private DkmAdminRoleMapper dkmAdminRoleMapper;

    @Resource
    private RedisTemplate redisTemplate;

    public PageResp selectForPage(int pageIndex, int pageSize, String username, String startTime, String endTime) {
        Page<DkmAdmin> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmAdmin> queryWrapper = Wrappers.<DkmAdmin>lambdaQuery()
                .like(StrUtil.isNotBlank(username), DkmAdmin::getUsername, username)
                .ge(StrUtil.isNotBlank(startTime), DkmAdmin::getCreateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmAdmin::getCreateTime, endTime);
        Page<DkmAdmin> dkmAdminPage = dkmAdminMapper.selectPage(page, queryWrapper);
        dkmAdminPage.getRecords().forEach(admin -> admin.setPassword(null));
        return PageResp.success("查询成功", page.getTotal(), dkmAdminPage.getRecords());
    }

    public PageResp selectRoleNameListById(int id) {
        List<String> roleNameList = dkmAdminMapper.selectRoleNameListById(id);
        return PageResp.success("查询成功", roleNameList);
    }

    public PageResp selectAllRole() {
        List<DkmRole> roleList = dkmRoleMapper.selectList(null);
        return PageResp.success("查询成功", roleList);
    }

    /**
     * 角色数量较少时,可以使用 for 循环进行插入
     *
     * @param adminVO VO 对象
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateAdminById(AdminVO adminVO) {
        DkmAdmin admin = new DkmAdmin();
        BeanUtils.copyProperties(adminVO, admin);
        LambdaUpdateWrapper<DkmAdmin> lambdaUpdateWrapper = new LambdaUpdateWrapper<DkmAdmin>().eq(DkmAdmin::getId, admin.getId())
                .set(DkmAdmin::getExtraInfo, admin.getExtraInfo())
                .set(DkmAdmin::getUsername, admin.getUsername());
        try {
            dkmAdminMapper.update(null, lambdaUpdateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return PageResp.success("用户名重复");
        }
        //删除此账户原有 账户-角色关系
        dkmAdminRoleMapper.deleteByAdminId(adminVO.getId());
        if (Objects.isNull(adminVO.getRoleId())) {
            return PageResp.success("更新成功");
        } else {
            DkmAdminRole dkmAdminRole = new DkmAdminRole();
            dkmAdminRole.setAdminId(adminVO.getId());
            //根据角色名称查询id,插入中间表
            dkmAdminRole.setRoleId(adminVO.getRoleId());
            dkmAdminRoleMapper.insert(dkmAdminRole);
            // 涉及到权限需要清除redis中的token
            // 根据用户名找到token 然后删除
            Boolean delete = redisTemplate.delete(adminVO.getUsername());
            return PageResp.success("更新成功");
        }


    }

    /**
     * 删除管理员的同时删除 管理员-角色表中的 管理员id
     *
     * @param id 账号id
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp deleteById(Integer id) {
        if (Objects.isNull(id)) {
            return PageResp.fail("用户id不能为空");
        }
        DkmAdmin dkmAdmin = dkmAdminMapper.selectById(id);
        dkmAdminMapper.deleteById(id);
        dkmAdminRoleMapper.delete(Wrappers.<DkmAdminRole>lambdaQuery().eq(DkmAdminRole::getAdminId, id));
        if (Objects.isNull(dkmAdmin)) {
            return PageResp.fail("用户不存在");
        }
        // 删除tokon
        String username = dkmAdmin.getUsername();
        // 根据用户名找到token 然后删除
        Boolean delete = redisTemplate.delete(username);
        return PageResp.success("删除成功");
    }

    public PageResp modifyPassword(String username, String password, String newPassword) {

        DkmAdmin admin = dkmAdminMapper.selectOne(Wrappers.<DkmAdmin>lambdaQuery()
                .eq(DkmAdmin::getUsername, username));
        if (admin != null && Objects.equals(admin.getPassword(), password)) {
            dkmAdminMapper.update(null, Wrappers.<DkmAdmin>lambdaUpdate()
                    .set(DkmAdmin::getPassword, newPassword)
                    .eq(DkmAdmin::getUsername, username));
            return PageResp.success("修改成功");
        }
        return PageResp.fail("修改失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public PageResp insert(InsertAdminVO insertAdminVO) {
        if (StrUtil.hasBlank(insertAdminVO.getUsername(), insertAdminVO.getPassword()) || Objects.isNull(insertAdminVO.getRoleId())) {
            return PageResp.fail(9001, "必填参数未传递");
        }
        DkmAdmin alreadyExistAdmin = dkmAdminMapper.selectOne(Wrappers.<DkmAdmin>lambdaQuery()
                .eq(DkmAdmin::getUsername, insertAdminVO.getUsername()));
        if (alreadyExistAdmin != null) {
            return PageResp.fail(500, "用户已存在");
        }
        DkmAdmin admin = new DkmAdmin();
        BeanUtils.copyProperties(insertAdminVO, admin);
        admin.setCreateTime(new Date());
        dkmAdminMapper.insert(admin);
        // 新建用户角色中间表
        Integer roleId = insertAdminVO.getRoleId();
        DkmAdminRole dkmAdminRole = new DkmAdminRole();
        dkmAdminRole.setAdminId(admin.getId());
        dkmAdminRole.setRoleId(roleId);
        dkmAdminRoleMapper.insert(dkmAdminRole);
        return PageResp.success("新增成功");
    }
}
