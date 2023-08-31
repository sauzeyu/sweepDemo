package com.vecentek.back.util;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("springContextUtils")
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取所有实现类
     */
    public static <T> List<T> getBeanListOfType(Class<T> clazz) {
        Map<String, T> map = applicationContext.getBeansOfType(clazz);
        return new ArrayList<>(map.values());
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }
}
