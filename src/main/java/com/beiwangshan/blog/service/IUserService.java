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
    ResponseResult register(BwsUser bwsUser,
                            String emailCode,
                            String captchaCode,
                            String captchaKey,
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

    /**
     * 检查用户登录状态
     *
     * @return
     */
    BwsUser checkBwsUser();

        /**
         * 获取用户的信息
         *   1.从数据库里获取
         *   2.判断结果
         *     - 如果不存在，就返回不存在
         *     - 如果存在，就复制对象，清空携带的密码，email，注册和登录的IP
         *   3.返回结果
     *
     * @param userId
     * @return
     */
    ResponseResult getUserInfo(String userId);

    /**
     * 用户在修改用户信息之前验证 email
     * 保证email的唯一性
     * @param email
     * @return
     */
    ResponseResult checkEmail(String email);

    /**
     * 检查用户名是否已经注册，保证其唯一性
     * @param userName
     * @return
     */
    ResponseResult checkUserName(String userName);

    /**
     * 更新用户信息
     * @param userId 查询
     * @param bwsUser 查询
     * @return
     */
    ResponseResult updateUserInfo(String userId, BwsUser bwsUser);

    /**
     * 通过userId来删除用户，在此之前需要判断操作用户的权限
     * @param userId
     * @return
     */
    ResponseResult deleteUserById(String userId);

    /**
     * 查询用户列表
     *  需要管理员权限，分页查询
     * @param page
     * @param size
     * @return
     */
    ResponseResult listUser(int page, int size);

    /**
     * 更新用户的密码
     * @param verifyCode
     * @param bwsUser
     * @return
     */
    ResponseResult updatePassword(String verifyCode, BwsUser bwsUser);

    /**
     * 更新用户的邮箱
     * @param email
     * @param verifyCode
     * @return
     */
    ResponseResult updateEmail(String email, String verifyCode);

    /**
     * 退出登录
     * @return
     */
    ResponseResult doLogout();
}
