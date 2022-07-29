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
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:35
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
     * @param userName  管理员姓名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 管理员列表
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String userName,
                                  String startTime,
                                  String endTime) {
        return this.dkmAdminService.selectForPage(pageIndex, pageSize, userName, startTime, endTime);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 需要查询的id
     * @return 单条数据
     */
    @GetMapping(value = "/selectRoleNameListById")
    public PageResp selectAdminInfoById(@RequestParam(name = "id") int id) {
        return this.dkmAdminService.selectRoleNameListById(id);
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @GetMapping(value = "/selectAllRole")
    public PageResp selectAllRole() {
        return this.dkmAdminService.selectAllRole();
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @PostMapping(value = "/updateAdminById")
    public PageResp updateAdminById(@RequestBody AdminVO adminVO) {
        return this.dkmAdminService.updateAdminById(adminVO);
    }

    @PostMapping(value = "/modifyPassword")
    public PageResp modifyPassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
        return this.dkmAdminService.modifyPassword(username, password, newPassword);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 删除是否成功
     */
    @PostMapping(value = "/deleteById")
    public PageResp deleteById(@RequestParam("id") Integer id) {
        return this.dkmAdminService.deleteById(id);
    }


    @PostMapping(value = "/insert")
    public PageResp insert(@RequestBody InsertAdminVO insertAdminVO) {
        return this.dkmAdminService.insert(insertAdminVO);
    }
}
