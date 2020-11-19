package com.beiwangshan.blog.utils;

/**
 * @className: com.beiwangshan.blog.utils-> Contants
 * @description: 常量集合
 * @author: 曾豪
 * @createDate: 2020-11-18 0:56
 * @version: 1.0
 * @todo:
 */
public interface Contants {

    //    页面默认的分页大小
    int DEFAULT_SIZE = 20;

    interface User {
        //管理员默认角色
        String ROLE_ADMIN = "ROLE_ADMIN";
        //管理员默认头像
        String DEFAULT_AVATAR = "https://profile.csdnimg.cn/8/1/1/2_simon_477";
        //默认的状态，是可用的
        String DEFAULT_STATE = "1";
        //图灵验证码的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        //邮箱发送的key
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
//        邮箱发送的地址
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";

    }

    interface Settings {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
    }


}
