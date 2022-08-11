package com.vecentek.back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pro")
@Data
public class ProConfig {
    private String sysDate = "";
}
