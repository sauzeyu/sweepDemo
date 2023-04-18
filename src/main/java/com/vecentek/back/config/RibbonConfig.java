package com.vecentek.back.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import com.vecentek.back.log.LoggingClientHttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author EdgeYu
 * @since 2023/03/29 10:40
 **/
@Configuration
public class RibbonConfig {
    /**
     * 配置RestTemplate，使用@LoadBalanced注解启用Ribbon负载均衡
     *
     * @return {@link RestTemplate}
     * @author EdgeYu
     * @date 2023-04-18 14:53
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // 创建连接池，设置最大连接数和每个路由的最大连接数
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        // 配置请求超时时间
        // 连接超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000)
                // 请求超时时间
                .setConnectionRequestTimeout(1000)
                // 响应超时时间
                .setSocketTimeout(5000).build();

        // 使用HttpClient创建RestTemplate，设置超时时间和连接池
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(5000);
        factory.setConnectionRequestTimeout(1000);

        RestTemplate restTemplate = new RestTemplate(factory);
        // 设置错误处理器
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        // 设置日志拦截器
        restTemplate.setInterceptors(Collections.singletonList(new LoggingClientHttpRequestInterceptor()));
        return restTemplate;
    }


    /**
     * 配置负载均衡策略，使用基于响应时间的加权负载均衡策略
     *
     * @return {@link IRule}
     * @author EdgeYu
     * @date 2023-04-18 14:52
     */
    @Bean
    public IRule ribbonRule() {
        return new WeightedResponseTimeRule();
    }
}


