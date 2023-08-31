package com.vecentek.back.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 手机用户信息(DkmUser)表服务实现类
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:26:34
 */

@Service("dkmUserService")
public class DkmUserServiceImpl {
    @Resource
    private DkmUserMapper dkmUserMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public PageResp selectById(Integer id) {
        DkmUser dkmUser = this.dkmUserMapper.selectById(id);
        if (dkmUser != null) {
            dkmUser.setPhone(DesensitizedUtil.mobilePhone(dkmUser.getPhone()));
            return PageResp.success("查询成功", dkmUser);
        }
        return PageResp.fail("查询失败，用户不存在");
    }

}
