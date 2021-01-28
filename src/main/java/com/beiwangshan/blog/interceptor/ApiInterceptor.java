package com.beiwangshan.blog.interceptor;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.CookieUtils;
import com.beiwangshan.blog.utils.RedisUtils;
import com.beiwangshan.blog.utils.TextUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @className: com.beiwangshan.blog.interceptor-> ApiInterceptor
 * @description: api重复提交的问题
 * @author: 曾豪
 * @createDate: 2020-12-20 12:10
 * @version: 1.0
 * @todo:
 */
@Component
@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            //某一些提交需要拦截
            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            String name = method.getName();
//            log.info("method name == > " + name);
            CheckTooFrequentCommit methodAnnotation = handlerMethod.getMethodAnnotation(CheckTooFrequentCommit.class);
            if (methodAnnotation != null) {
                String methodName = handlerMethod.getMethod().getName();
                //所有提交内容的方法，必须是用户登录的。所以使用token作为可以来记录请求的频率
                String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
                log.info("tokenKey ==> || =" + tokenKey);
                if (!TextUtils.isEmpty(tokenKey)) {
                    String hacCommit = (String) redisUtils.get(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey);
                    if (!TextUtils.isEmpty(hacCommit)) {
                        //从redis里面获取，判断是否存在，如果存在，则返回 =》提交太频繁
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        ResponseResult failed = ResponseResult.FAILED("提交过于频繁，请稍后重试。");
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(failed));
                        writer.flush();
                        return false;

                    } else {
                        //如果不存在，说明可以提交，并且记录此次提交，有效期为30秒
                        redisUtils.set(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey + methodName, "true", Constants.TimeValueInSecond.SECOND_10);
                    }

                }
                //去判断是否真的提交太频繁了！

                log.info("检查提交次数是否太频繁 ~~ ");
            }
        }
        //true表示放行，false表示拦截
        return true;
    }
}
