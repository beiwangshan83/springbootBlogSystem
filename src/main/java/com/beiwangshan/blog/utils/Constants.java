package com.beiwangshan.blog.utils;

/**
 * @className: com.beiwangshan.blog.utils-> Contants
 * @description: 常量集合
 * @author: 曾豪
 * @createDate: 2020-11-18 0:56
 * @version: 1.0
 * @todo:
 */
public interface Constants {


    interface User {
        //管理员默认角色
        String ROLE_ADMIN = "role_admin";
        //注册默认角色
        String ROLE_NORMAL = "role_normal";
        //管理员默认头像
        String DEFAULT_AVATAR = "https://profile.csdnimg.cn/8/1/1/2_simon_477";
        //默认的状态，是可用的
        String DEFAULT_STATE = "1";
        //用户被禁止的状态 state = 0
        String DENIAL_STATE = "0";
        //图灵验证码的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        //图灵验证码的key
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        //图灵验证码key的长度，100年之间，默认是 13位
        int CAPTCHA_KEY_LENGTH = 13;
        //邮箱发送的key
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        //邮箱发送的地址
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        //token秘钥
        String KEY_TOKEN = "key_token_";
        //token秘钥
        String COOKIE_TOKEN_KEY = "bws_blog_token";


        /**
         * 注册(register)：如果已经存在，提示该邮箱已经注册
         * 找回密码(forget)：如果没有注册，提示该邮箱没有注册
         * 修改邮箱(update)：（新的邮箱）如果已经注册了，提示该邮箱已经注册
         */
        String LOGIN_TYPE_REGISTER = "register";
        String LOGIN_TYPE_FORGET = "forget";
        String LOGIN_TYPE_UPDATE = "update";
    }

    interface Settings {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
    }

    /**
     * 时间类
     * 单位是 s
     */
    interface TimeValueInSecond {
        int MIN = 60;//一分钟
        int MIN_5 = MIN * 5;// 5 分钟
        int MIN_10 = MIN * 10;// 10 分钟
        int HOUR_1 = MIN * 60;//一小时
        int HOUR_2 = HOUR_1 * 2;//两小时
        int DAY = HOUR_1 * 2;//一天
        int WEAK = DAY * 7;//一个周
        int MONTH = DAY * 30;//一个月
        int YEAR = MONTH * 12;//一年
    }

    interface TimeValueInMillions {
        long MIN = 60 * 1000;//一分钟
        long MIN_5 = MIN * 5;// 5 分钟
        long MIN_10 = MIN * 10;// 10 分钟
        long HOUR_1 = MIN * 60;//一小时
        long HOUR_2 = HOUR_1 * 2;//两小时
        long DAY = HOUR_1 * 2;//一天
        long WEAK = DAY * 7;//一个周
        long MONTH = DAY * 30;//一个月
        long YEAR = MONTH * 12;//一年
    }

    /**
     * 页面大小
     */
    interface Page {
        //默认的page大小
        int DEFAULT_PAGE = 1;
        //较小的分页大小
        int MIN_SIZE = 10;
        //默认大小
        int DEFAULT_SIZE = 20;
    }

    /**
     * 图片相关常量
     */
    interface Image {
        String PREFIX = "image/";
        String IMAGE_PNG = "png";
        String IMAGE_GIF = "gif";
        String IMAGE_JPG = "jpg";

        String IMAGE_PNG_WITH_PREFIX = PREFIX + "png";
        String IMAGE_GIF_WITH_PREFIX = PREFIX + "gif";
        String IMAGE_JPG_WITH_PREFIX = PREFIX + "jpg";
    }


}
