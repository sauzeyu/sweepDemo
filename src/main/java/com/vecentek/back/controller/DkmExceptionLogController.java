package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmFunctionalAbnormalServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/10 15:03
 */
@RestController
@RequestMapping("/dkmlog")
public class DkmExceptionLogController {

    @Resource
    DkmFunctionalAbnormalServiceImpl dkmFunctionalAbnormalService;
    /**
     * 查询所有业务id和业务
     *
     * @return {@link PageResp}
     * @author liujz
     * @date 2023/4/10 15:03
     */
    @GetMapping(value = "/selectBusiness")
    public PageResp selectBusiness(
    ) {

        return this.dkmFunctionalAbnormalService.selectBusiness();

    }
}
