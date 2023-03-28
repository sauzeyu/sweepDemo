package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmDkmKeyLifecycleServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-19 14:30
 */

@RestController
@RequestMapping("/dkmKeyLifecycle")
public class DkmKeyLifecycleController {

    @Resource
    private DkmDkmKeyLifecycleServiceImpl dkmKeyLifecycleService;


    @GetMapping("/selectForPageByKeyId")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  @RequestParam String keyId) {

        return dkmKeyLifecycleService.selectForPage(pageIndex, pageSize, keyId);
    }
}
