package com.vecentek.back.controller;

import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.service.impl.DkmRouterServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 动态获取前端路由
 */
@RestController
@RequestMapping("/dkmRouter")
public class DkmRouterController {

    @Resource
    private DkmRouterServiceImpl dkmRouterService;

    @RequestMapping(value = "/getPageRouter", method = RequestMethod.GET)
    public PageResp selectForPage(@RequestParam(name = "id") Integer id) {
        return dkmRouterService.getPageRouter(id);
    }

    @RequestMapping(value = "/testException", method = RequestMethod.GET)
    public void testException() throws DiagnosticLogsException {
        throw new DiagnosticLogsException("01","5004");
    }

}
