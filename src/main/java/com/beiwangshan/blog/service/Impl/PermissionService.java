package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.CookieUtils;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: com.beiwangshan.blog.service-> permissionService
 * @description: 角色权限管理的service
 * @author: 曾豪
 * @createDate: 2020-11-22 17:57
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service("permission")
public class PermissionService {

    @Autowired
    private IUserService userService;

    /**
     * 判断是不是管理员
     * @return
     */
    public boolean admin(){
//        拿到request 和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

//        拿到cookie进行判断，如果cookie都没有，肯定没有权限
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        if (TextUtils.isEmpty(tokenKey)) {
//            没有权限的用户 ==> 未登录
            return false;
        }

        BwsUser checkBwsUser = userService.checkBwsUser(request, response);
        if (checkBwsUser == null) {
//            没有权限的用户  ==> 未登录
            return false;
        }

        if (Constants.User.ROLE_ADMIN.equals(checkBwsUser.getRoles())) {
//            这是管理员，是有权限的，可以返回true
            return true;
        }
        return false;

    }
}
