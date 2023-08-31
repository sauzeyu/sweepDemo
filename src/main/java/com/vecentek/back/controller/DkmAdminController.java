package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmAdminServiceImpl;
import com.vecentek.back.vo.AdminVO;
import com.vecentek.back.vo.InsertAdminVO;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用户管理模块
 * @author EdgeYu
 * @since 2022-03-11 10:35
 * @version 1.0
 */
@RestController
@RequestMapping("/dkmAdmin")
public class DkmAdminController {
    /**
     * 服务对象
     */
    @Resource
    private DkmAdminServiceImpl dkmAdminService;


    /**
     * 分页查询管理员信息
     *
     * @param pageIndex 起始位置
     * @param pageSize  起始页
     * @param username  管理员姓名
     * @param startTime 开始时间
     * @param endTime   结束时间
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:03
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String username,
                                  String startTime,
                                  String endTime) {
        return this.dkmAdminService.selectForPage(pageIndex, pageSize, username, startTime, endTime);
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 需要查询的id
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:03
     */
    @GetMapping(value = "/selectRoleNameListById")
    public PageResp selectAdminInfoById(@RequestParam(name = "id") int id) {
        return this.dkmAdminService.selectRoleNameListById(id);
    }


    /**
     * 查询所有角色
     *
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:04
     */
    @GetMapping(value = "/selectAllRole")
    public PageResp selectAllRole() {
        return this.dkmAdminService.selectAllRole();
    }


    /**
     * 更新用户ID更新用户信息
     *
     * @param adminVO 用户展示实体
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:04
     */
    @PostMapping(value = "/updateAdminById")
    public PageResp updateAdminById(@RequestBody AdminVO adminVO) {
        return this.dkmAdminService.updateAdminById(adminVO);
    }

    /**
     * 管理员根据用户ID重置密码
     *
     * @param adminVO 用户展示实体
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:05
     */
    @PostMapping(value = "/resetPasswordById")
    public PageResp resetPasswordById(@RequestBody AdminVO adminVO) {
        return this.dkmAdminService.resetPasswordById(adminVO);
    }

    /**
     * 用户修改密码
     *
     * @param username 用户名称
     * @param password 用户旧密码
     * @param newPassword 用户新密码
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:05
     */
    @PostMapping(value = "/modifyPassword")
    public PageResp modifyPassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
        return this.dkmAdminService.modifyPassword(username, password, newPassword);
    }


    /**
     * 删除用户 根据用户id删除
     *
     * @param id 用户id
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:06
     */
    @PostMapping(value = "/deleteById")
    public PageResp deleteById(@RequestParam("id") Integer id) {
        return this.dkmAdminService.deleteById(id);
    }

    /**
     * 插入一个用户
     *
     * @param insertAdminVO 用户插入实体
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023-03-14 10:07
     */
    @PostMapping(value = "/insert")
    public PageResp insert(@RequestBody InsertAdminVO insertAdminVO) {
        return this.dkmAdminService.insert(insertAdminVO);
    }
}
