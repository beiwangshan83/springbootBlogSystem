package com.beiwangshan.blog.controller.user;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.user-> UserApi
 * @description: 用户相关的接口
 * @author: 曾豪
 * @createDate: 2020-11-17 20:10
 * @version: 1.0
 * @todo:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserApi {

    /**
     * 初始化管理员账号 init-admin
     * @param bwsUser
     * @return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody BwsUser bwsUser){

        return ResponseResult.SUCCESS();
    }

    /**
     * 用户注册
     * @param bwsUser
     * @return
     */
    @PostMapping
    public ResponseResult register(@RequestBody BwsUser bwsUser){

        return null;
    }

    /**
     * 用户登录 captcha 图灵验证码
     * @param captcha
     * @param bwsUser
     * @return
     */
    @PostMapping("/{captcha}")
    public ResponseResult login(@PathVariable("captcha")String captcha, @RequestBody BwsUser bwsUser){
        return null;
    }


    /**
     * 获取图灵验证码
     * @return
     */
    @GetMapping("/captcha")
    public ResponseResult getCaptcha(){
        return null;
    }


    /**
     * 发送邮件
     * @param emailAddress
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(@RequestParam("email")String emailAddress){
        return null;
    }

    /**
     * 修改密码
     * @param bwsUser
     * @return
     */
    @PutMapping("/password")
    public ResponseResult updatePasssword(@RequestBody BwsUser bwsUser){

        return null;
    }

    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId")String userId,@RequestBody BwsUser bwsUser){

        return null;
    }

    @PutMapping
    public ResponseResult updateUserInfo(@RequestBody BwsUser bwsUser){

        return null;
    }

}
