package com.vecentek.back.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2021-11-26 15:15
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {


    /**
     * 定义切点,对 Controller 包下的请求进行处理
     */
    @Pointcut("execution(public * com.vecentek.back.controller..*.*(..))")
    public void controllerLog() {

    }

    @Before("controllerLog()")
    public void logBeforeController(JoinPoint joinPoint) {

        //RequestContextHolder 是 SpringMVC 提供的 Request 的容器
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        HttpServletRequest request;
        if (requestAttributes != null) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
            log.info("请求 URL：{}", request.getRequestURL().toString());
            log.info("请求的：{}", request.getMethod());
            log.info("IP 地址：{}", request.getRemoteAddr());
            log.info("请求参数：{}", Arrays.toString(joinPoint.getArgs()));
            //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
            log.info("包名+类名：{},方法名：{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        }

    }

}