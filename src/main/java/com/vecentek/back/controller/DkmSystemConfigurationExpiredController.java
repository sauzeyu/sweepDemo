//package com.vecentek.back.controller;
//
//
//
//import com.vecentek.back.entity.DkmSystemConfigurationExpired;
//import com.vecentek.back.service.impl.DkmSystemConfigurationExpiredServiceImpl;
//import com.vecentek.common.response.PageResp;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//
///**
// * (DkmSystemConfigurationExpired)表控制层
// *
// * @author liujz
// * @since 2023-01-03 15:41:08
// */
//@RestController
//@RequestMapping("/dkmSystemConfigurationExpired")
//public class DkmSystemConfigurationExpiredController  {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private DkmSystemConfigurationExpiredServiceImpl dkmSystemConfigurationExpiredService;
//
//
//    /**
//     * @description: 查询是否存在过期数据
//     * @author liujz
//     * @date 2023/1/3 15:47
//     * @version 1.0
//     */
//    @GetMapping("/selectForExpiration")
//    public PageResp selectForExpiration() {
//        return dkmSystemConfigurationExpiredService.selectForExpiration();
//    }
//
//
//
//    /**
//     * @description: 超级管理员配置钥匙记录过期时间
//     * @author liujz
//     * @date 2023/1/3 15:51
//     * @version 1.0
//     */
//    @PostMapping("/saveOrUpdateConfigExpired")
//    public PageResp saveOrUpdateConfigExpired(DkmSystemConfigurationExpired dkmSystemConfigurationExpired) {
//        return dkmSystemConfigurationExpiredService.saveOrUpdateConfigExpired(dkmSystemConfigurationExpired);
//    }
//
//
//}
//
