package com.beiwangshan.blog.utils;

import com.beiwangshan.blog.pojo.BwsUser;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.beiwangshan.blog.utils-> ClaimsUtils
 * @description: token解析的工具类
 * @author: 曾豪
 * @createDate: 2020-11-20 10:46
 * @version: 1.0
 * @todo:
 */
@Slf4j
public class ClaimsUtils {

    public static final String ID = "id";
    public static final String USERNAME = "user_name";
    public static final String ROLES = "roles";
    public static final String AVATAR = "avatar";
    public static final String EMAIL = "email";
    public static final String SIGN = "sign";

    public static Map<String, Object> bwsUser2Claims(BwsUser bwsUser) {
        Map<String, Object> claims = new HashMap<>();
        //放入数据
        claims.put(ID, bwsUser.getId());
        claims.put(USERNAME, bwsUser.getUserName());
        claims.put(ROLES, bwsUser.getRoles());
        claims.put(AVATAR, bwsUser.getAvatar());
        claims.put(EMAIL, bwsUser.getEmail());
        claims.put(SIGN, bwsUser.getSign());
        return claims;
    }

    public static BwsUser cliams2BwsUser(Claims claims) {
        BwsUser bwsUser = new BwsUser();
        log.info("cliams2BwsUser 传入的 claims" + claims);
        String id = (String) claims.get(ID);
        log.info("cliams2BwsUser get 的ID " + id);
        bwsUser.setId(id);
        String userName = (String) claims.get(USERNAME);
        bwsUser.setUserName(userName);
        String roles = (String) claims.get(ROLES);
        bwsUser.setRoles(roles);
        String avatar = (String) claims.get(AVATAR);
        bwsUser.setAvatar(avatar);
        String email = (String) claims.get(EMAIL);
        bwsUser.setEmail(email);
        String sign = (String) claims.get(SIGN);
        bwsUser.setSign(sign);

        log.info("cliams2BwsUser 生成的 user " + bwsUser);
        return bwsUser;

    }
}
