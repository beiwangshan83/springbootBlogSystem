package com.beiwangshan.blog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: com.beiwangshan.blog.utils-> textUtils
 * @description: 文字校验相关的工具类
 * @author: 曾豪
 * @createDate: 2020-11-18 0:37
 * @version: 1.0
 * @todo:
 */
public class TextUtils {

    //    邮箱验证正则表达式
   public static final String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 判断文字是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    /**
     * 验证是否是一个有效的邮箱地址
     * @param emailAddress
     * @return
     */
    public static boolean isEmailAddressOk(String emailAddress) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }
}
