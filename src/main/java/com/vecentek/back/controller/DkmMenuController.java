package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmMenuServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-18 14:30
 */
@RestController
@RequestMapping("/dkmMenu")
public class DkmMenuController {

    @Resource
    private DkmMenuServiceImpl dkmMenuService;

    @GetMapping(value = "/selectAll")
    public PageResp selectAll() {
        return dkmMenuService.selectAll();
    }

    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String title,
                                  String icon,
                                  String href) {
        return dkmMenuService.selectForPage(pageIndex,pageSize,title,icon,href);
    }

    @GetMapping(value = "/selectMenuByRoleId")
    public PageResp selectMenuByRoleId(@RequestParam("id") Integer id) {
        return dkmMenuService.selectMenuByRoleId(id);
    }


    @GetMapping(value = "/selectByParentId")
    public PageResp selectByParentId(@RequestParam("parentId") Integer parentId) {
        return dkmMenuService.selectByParentId(parentId);
    }
}
