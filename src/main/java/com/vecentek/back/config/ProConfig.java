package com.vecentek.back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sharding-jdbc 分表起始时间
 *
 * @author liujz
 * @date 2023-04-18 14:32
 */
@Component
@ConfigurationProperties(prefix = "pro")
@Data
public class ProConfig {
    private String sysDate = "";
}
