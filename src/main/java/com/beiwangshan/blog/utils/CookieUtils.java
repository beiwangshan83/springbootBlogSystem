package com.beiwangshan.blog.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: com.beiwangshan.blog.utils-> CookieUtils
 * @description: cookie工具类
 * @author: 曾豪
 * @createDate: 2020-11-20 9:12
 * @version: 1.0
 * @todo:
 */
@Slf4j
public class CookieUtils {

    /**
     * 设置cookie的默认时间
     */
    public static final int default_age = Constants.TimeValueInSecond.YEAR;

    public static final String domain = "localhost";

    /**
     * 设置cookie值
     *
     * @param response
     * @param key
     * @param value
     */
    public static void setUpCookie(HttpServletResponse response, String key, String value) {
        setUpCookie(response, key, value, default_age);
    }

    public static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param key
     */
    public static void deleteCookie(HttpServletResponse response, String key) {
        setUpCookie(response, key, null, 0);
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            log.info("cookies is null");
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}