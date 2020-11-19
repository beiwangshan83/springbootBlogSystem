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
 * @todo:
 *  基本流程：
 *      前端调用初始换账号接口（传入 用户名，密码 Email） ==>  后端获取到参数
 *      判断 是否已经初始化过 ==> 是 ==> 返回失败的结果
 *                            否 ==> 添加一个管理员账号 ==> 修改设置标记 ==> 返回成功的结果
 */
public interface IUserService {

    ResponseResult initManagerAccount(BwsUser bwsUser, HttpServletRequest request);

    void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception;
}
