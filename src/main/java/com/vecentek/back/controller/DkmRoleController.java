package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmRoleServiceImpl;
import com.vecentek.back.vo.InsertRoleVO;
import com.vecentek.back.vo.RoleDTO;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-17 15:29
 */
@RestController
@RequestMapping("/dkmRole")
public class DkmRoleController {


    /**
     * 服务对象
     */
    @Resource
    private DkmRoleServiceImpl dkmRoleService;



    /**
     * 分页查询管理员信息
     *
     * @param pageIndex 起始位置
     * @param pageSize  起始页
     * @param roleName  角色名称
     * @param code      角色代码
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return com.vecentek.common.response.PageResp
     * @author EdgeYu
     * @date 2022-05-20 11:45
     */

    @RequestMapping(value = "/selectForPage", method = RequestMethod.GET)
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String roleName,
                                  Integer code,
                                  String startTime,
                                  String endTime) {

        return this.dkmRoleService.selectForPage(pageIndex, pageSize, roleName, code, startTime, endTime);
    }


    @RequestMapping(value = "/updateRoleById", method = RequestMethod.POST)
    public PageResp updateRoleById(@RequestBody RoleDTO role) {

        return this.dkmRoleService.updateRoleById(role);
    }


    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public PageResp deleteById(@RequestParam("id") Integer id) {

        return this.dkmRoleService.deleteById(id);
    }


    @PostMapping(value = "/insert")
    public PageResp insert(@RequestBody InsertRoleVO roleVO) {
        return this.dkmRoleService.insert(roleVO);
    }

}
