package com.vecentek.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.service.impl.DkmMenuServiceImpl;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private DkmMenuMapper dkmMenuMapper;

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
                                  String code,
                                  String startTime,
                                  String endTime) {

        return this.dkmRoleService.selectForPage(pageIndex, pageSize, roleName, code, startTime, endTime);
    }


    @RequestMapping(value = "/updateRoleById", method = RequestMethod.POST)
    public PageResp updateRoleById(@RequestBody RoleDTO role) {
        role.setMenuList(menuParentId(role.getMenuList()));
        return this.dkmRoleService.updateRoleById(role);
    }


    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public PageResp deleteById(@RequestParam("id") Integer id) {

        return this.dkmRoleService.deleteById(id);
    }


    @PostMapping(value = "/insert")
    public PageResp insert(@RequestBody InsertRoleVO roleVO) {
        roleVO.setMenuList(menuParentId(roleVO.getMenuList()));

        return this.dkmRoleService.insert(roleVO);
    }

    private DkmMenu selectParentMenu(DkmMenu subMenu) {
        return dkmMenuMapper.selectOne(new LambdaQueryWrapper<DkmMenu>().eq(DkmMenu::getId, subMenu.getParentId()));
    }

    public List<String> menuParentId(List<String> list) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            DkmMenu menu = dkmMenuMapper.selectOne(new LambdaQueryWrapper<DkmMenu>().eq(DkmMenu::getId, Integer.parseInt(list.get(i))));
            if (menu.getParentId() != null) {
                DkmMenu superMenu = selectParentMenu(menu);
                list.add(superMenu.getId().toString());
                if (superMenu.getParentId() != null) {
                    DkmMenu topMenu = selectParentMenu(superMenu);
                    list.add(topMenu.getId().toString());
                }
            }
        }
        HashSet<String> set = new HashSet<>(list);
        List<String> collect = set.stream().collect(Collectors.toList());

        return collect;
    }
}
