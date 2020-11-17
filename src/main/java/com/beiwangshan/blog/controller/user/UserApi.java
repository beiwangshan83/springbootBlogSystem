package com.beiwangshan.blog.controller.user;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private IUserService userService;

    /**
     * 初始化管理员账号 init-admin
     * @param bwsUser
     * @return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody BwsUser bwsUser, HttpServletRequest request){
        log.info("username ==> "+ bwsUser.getUserName());
        log.info("password ==> "+ bwsUser.getPassword());
        log.info("email ==> "+ bwsUser.getEmail());
        return userService.initManagerAccount(bwsUser,request);
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
    @PutMapping("/password/{userId}")
    public ResponseResult updatePasssword(@PathVariable("userId")String userId,@RequestBody BwsUser bwsUser){

        return null;
    }

    /**
     * 获取用户信息
     * @param userId
     * @param bwsUser
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId")String userId,@RequestBody BwsUser bwsUser){

        return null;
    }

    /**
     * 更新用户信息
     * @param userId
     * @param bwsUser
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId")String userId,@RequestBody BwsUser bwsUser){

        return null;
    }

    /**
     * 获取用户列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listUser(@RequestParam("page")int page,@RequestParam("size")int size){

        return null;
    }

    /**
     * 删除用户，通过用户的ID
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId")String userId){
        return null;
    }

    @PutMapping("")
    public ResponseResult updateUser(){
        return null;
    }

}
