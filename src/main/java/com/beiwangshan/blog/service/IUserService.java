package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: com.beiwangshan.blog.service-> IUserService
 * @description: 初始化管理员账号的service
 * @author: 曾豪
 * @createDate: 2020-11-18 0:23
 * @version: 1.0
 * @todo: 基本流程：
 * 前端调用初始换账号接口（传入 用户名，密码 Email） ==>  后端获取到参数
 * 判断 是否已经初始化过 ==> 是 ==> 返回失败的结果
 * 否 ==> 添加一个管理员账号 ==> 修改设置标记 ==> 返回成功的结果
 */
public interface IUserService {

    /**
     * 新增管理员
     *
     * @param bwsUser
     * @param request
     * @return
     */
    ResponseResult initManagerAccount(BwsUser bwsUser, HttpServletRequest request);

    /**
     * 图灵验证码
     *
     * @param response
     * @param captchaKey
     * @throws Exception
     */
    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;

    /**
     * 发送邮件
     *
     * @param request
     * @param emailAddress
     * @return
     */
    ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress);

    /**
     * 用户注册
     *
     * @param bwsUser
     * @param emailCode
     * @param captchaCode
     * @param captchaKey
     * @param request
     * @return
     */
    ResponseResult register(BwsUser bwsUser, String emailCode,
                            String captchaCode, String captchaKey,
                            HttpServletRequest request);

    /**
     * 用户登录
     *
     * @param captcha
     * @param captchaKey
     * @param bwsUser
     * @param request
     * @param response
     * @return
     */
    ResponseResult doLogin(String captcha, String captchaKey,
                           BwsUser bwsUser, HttpServletRequest request,
                           HttpServletResponse response);
}
