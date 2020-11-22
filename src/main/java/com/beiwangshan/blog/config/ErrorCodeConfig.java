package com.beiwangshan.blog.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @className: com.beiwangshan.blog.config-> ErrorCodeConfig
 * @description: 错误代码的配置
 * @author: 曾豪
 * @createDate: 2020-11-22 19:20
 * @version: 1.0
 * @todo:
 */
@Configuration
public class ErrorCodeConfig  implements ErrorPageRegistrar {
    /**
     * Register pages as required with the given registry.
     *
     * @param registry the error page registry
     */
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN,"/403"));
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404"));
        registry.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED,"/405"));
        registry.addErrorPages(new ErrorPage(HttpStatus.GATEWAY_TIMEOUT,"/504"));
        registry.addErrorPages(new ErrorPage(HttpStatus.HTTP_VERSION_NOT_SUPPORTED,"/505"));
    }
}
