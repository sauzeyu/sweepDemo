package com.vecentek.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.management.ManagementFactory;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2021-11-30 15:42
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BackApplication {
    public static void main(String[] args) {
        ManagementFactory.getClassLoadingMXBean();
        SpringApplication.run(BackApplication.class, args);
    }
}
