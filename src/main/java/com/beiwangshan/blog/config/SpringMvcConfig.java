package com.beiwangshan.blog.config;

import com.beiwangshan.blog.interceptor.ApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @className: com.beiwangshan.blog.config-> SpringMvcConfig
 * @description: api拦截的配置文件
 * @author: 曾豪
 * @createDate: 2020-12-20 12:17
 * @version: 1.0
 * @todo:
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApiInterceptor apiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor);

    }
}
