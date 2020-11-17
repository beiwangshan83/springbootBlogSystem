package com.beiwangshan.blog.utils;

/**
 * @className: com.beiwangshan.blog.utils-> textUtils
 * @description: 文字校验相关的工具类
 * @author: 曾豪
 * @createDate: 2020-11-18 0:37
 * @version: 1.0
 * @todo:
 */
public class TextUtils {
    public static boolean isEmpty(String text){
        return text == null || text.length() == 0;
    }
}
