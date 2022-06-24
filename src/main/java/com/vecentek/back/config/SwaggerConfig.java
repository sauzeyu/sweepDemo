package com.vecentek.back.config;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger 配置文件
 *
 * @author ：EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:27:15
 */

// @Configuration
// @EnableSwagger2
public class SwaggerConfig {
    // @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否禁用swagger
                .enable(true)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DkServer-Back API文档")
                .description("DkServer-Back 测试接口文档")
                .version("2.0.0")
                .build();
    }
}

