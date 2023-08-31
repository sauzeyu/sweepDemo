package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmMenuServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 菜单(dkmMenu)表控制层
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-18 14:30
 */
@RestController
@RequestMapping("/dkmMenu")
public class DkmMenuController {

    @Resource
    private DkmMenuServiceImpl dkmMenuService;

    /**
     * 查询所有菜单信息
     * @return
     */
    @GetMapping(value = "/selectAll")
    public PageResp selectAll() {
        return dkmMenuService.selectAll();
    }

    /**
     * 分页查询菜单信息
     * @param pageIndex
     * @param pageSize
     * @param title
     * @param icon
     * @param href
     * @return
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String title,
                                  String icon,
                                  String href) {
        return dkmMenuService.selectForPage(pageIndex, pageSize, title, icon, href);
    }

    /**
     * 根据roleId查询菜单id
     * @param id
     * @return
     */
    @GetMapping(value = "/selectMenuByRoleId")
    public PageResp selectMenuByRoleId(@RequestParam("id") Integer id) {
        return dkmMenuService.selectMenuByRoleId(id);
    }


    /**
     * 根据菜单父id
     * @param parentId
     * @return
     */
    @GetMapping(value = "/selectByParentId")
    public PageResp selectByParentId(@RequestParam("parentId") Integer parentId) {
        return dkmMenuService.selectByParentId(parentId);
    }
}
