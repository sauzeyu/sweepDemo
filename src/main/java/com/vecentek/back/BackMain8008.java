package com.vecentek.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2021-11-30 15:42
 */
@SpringBootApplication
@EnableAsync
public class BackMain8008 {
    public static void main(String[] args) {
        SpringApplication.run(BackMain8008.class, args);
    }
}
