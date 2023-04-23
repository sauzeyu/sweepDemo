package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmFunctionalAbnormalServiceImpl;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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
@Slf4j
public class DkmExceptionLogController {
    @Autowired
    private ServerProperties serverProperties; // 注入 ServerProperties
    @Resource
    private DkmFunctionalAbnormalServiceImpl dkmFunctionalAbnormalService;
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

        return dkmFunctionalAbnormalService.selectBusiness();

    }

    @GetMapping("/ip")
    public String getIp() {
        //TomcatWebServer bean = SpringContextUtil.getBean(TomcatWebServer.class);
        //Connector connector = bean.getTomcat().getConnector(); // 获取 Connector 对象
        //int port = connector.getLocalPort(); // 获取绑定的端口号
        Integer port = serverProperties.getPort();
        System.out.println("ip: "+port);
            log.info("ip: "+port);
            log.error("ip: "+port);
            return String.valueOf(port);

    }



}
