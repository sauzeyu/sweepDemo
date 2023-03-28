package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmUserServiceImpl;
import com.vecentek.common.response.PageResp;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 手机用户信息(DkmUser)表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:26:34
 */

@Validated
@RestController
@RequestMapping("/dkmUser")
public class DkmUserController {
    /**
     * 服务对象
     */
    @Resource
    private DkmUserServiceImpl dkmUserServiceImpl;


    /**
     * 通过id查询单条用户信息
     *
     * @param id 主键
     * @return 实例对象
     */
    @GetMapping(value = "/selectById")
    public PageResp selectById(@RequestParam Integer id) {
        return this.dkmUserServiceImpl.selectById(id);
    }

    @GetMapping(value = "/token")
    public PageResp token(@RequestParam String token) {
        if (ObjectUtils.isNotEmpty(token)){

            return PageResp.success("令牌验证成功");
        }else {
            return PageResp.fail("令牌验证失败");
        }
    }

}
