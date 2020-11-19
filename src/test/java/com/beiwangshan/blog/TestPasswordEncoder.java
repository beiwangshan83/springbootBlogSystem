package com.beiwangshan.blog;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @className: com.beiwangshan.blog-> TestPasswordEncoder
 * @description: 用户密码加密的测试
 * @author: 曾豪
 * @createDate: 2020-11-18 19:14
 * @version: 1.0
 * @todo:
 */
public class TestPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoder = passwordEncoder.encode("123456");
        System.out.println("encode ==> " + encoder);
//        $2a$10$r3VWTd/lUw0pVe2nyh6mAudaTpseglK7XRQpObZ5hRRpLUD.lHLYa
//        $2a$10$JzwCvDyyHvBZEEHVEKDFZ.x1s63Uea1f6HoAWKKxnGD2K8.TeOpYy
//        每次都会不一样，他有自己的验证机制

        /**
         * 验证登录流程：
         *      1.用户提交明文密码：123456
         *      2.跟数据库的密文进行对比，验证是否争取
         *      3.这不算是解码，而算是一种匹配
         */

        String originalPassword = "123456";
       boolean matchesPassword = passwordEncoder.matches(originalPassword,"$2a$10$r3VWTd/lUw0pVe2nyh6mAudaTpseglK7XRQpObZ5hRRpLUD.lHLYa");
        System.out.println("密码是正确的 ===> " + matchesPassword);
    }
}
