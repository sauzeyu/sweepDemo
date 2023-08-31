package com.vecentek.back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * @author liujz
 * @date 2022/8/15
 * @apiNote
 */
@Component
@RefreshScope
@Data
@ConfigurationProperties(prefix = "test")
public class TestProperties {
    private Integer flag;
}
