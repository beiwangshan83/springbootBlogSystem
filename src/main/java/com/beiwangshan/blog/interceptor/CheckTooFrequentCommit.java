package com.beiwangshan.blog.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className: com.beiwangshan.blog.interceptor-> CheckTooFrequentCommit
 * @description: 频繁请求的注解
 * @author: 曾豪
 * @createDate: 2020-12-20 18:12
 * @version: 1.0
 * @todo:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTooFrequentCommit {
}
