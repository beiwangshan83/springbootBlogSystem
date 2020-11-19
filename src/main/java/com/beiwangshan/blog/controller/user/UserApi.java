package com.beiwangshan.blog.controller.user;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Random random;

    /**
     * 初始化管理员账号 init-admin
     *
     * @param bwsUser
     * @return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody BwsUser bwsUser, HttpServletRequest request) {
        log.info("username ==> " + bwsUser.getUserName());
        log.info("password ==> " + bwsUser.getPassword());
        log.info("email ==> " + bwsUser.getEmail());
        return userService.initManagerAccount(bwsUser, request);
    }

    /**
     * 用户注册
     *
     * @param bwsUser
     * @return
     */
    @PostMapping
    public ResponseResult register(@RequestBody BwsUser bwsUser) {
//        1.检查当前用户是否已经注册
//        2.检查邮箱格式是否正确（前端+后端都可以的）
//        3.检查邮箱是否已经注册
//        4.检查邮箱验证码是都正确
//        5.检查图灵验证码是否正确

//        达到注册的条件  ps:前端可以对用户名等进行校验
//        6.对密码进行加密
//        7.补全数据 包括ip，角色，创建时间，更新时间
//        8.保存到数据库
//        9.返回结果


        return null;
    }

    /**
     * 用户登录 captcha 图灵验证码
     *
     * @param captcha
     * @param bwsUser
     * @return
     */
    @PostMapping("/{captcha}")
    public ResponseResult login(@PathVariable("captcha") String captcha, @RequestBody BwsUser bwsUser) {
        return null;
    }






    /**
     * 获取图灵验证码
     * 有效时长是 10 分钟
     * 可以在 redisUtil.set(Contants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10); 中更改
     * @return
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchaKey) throws Exception {
        try{
            userService.createCaptcha(response,captchaKey);
        }catch (Exception e){
            log.error(e.toString());
        }
    }


    /**
     * 发送邮件
     *
     * @param emailAddress
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(@RequestParam("email") String emailAddress) {
        return null;
    }

    /**
     * 修改密码
     *
     * @param bwsUser
     * @return
     */
    @PutMapping("/password/{userId}")
    public ResponseResult updatePasssword(@PathVariable("userId") String userId, @RequestBody BwsUser bwsUser) {

        return null;
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @param bwsUser
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId, @RequestBody BwsUser bwsUser) {

        return null;
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param bwsUser
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId") String userId, @RequestBody BwsUser bwsUser) {

        return null;
    }

    /**
     * 获取用户列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listUser(@RequestParam("page") int page, @RequestParam("size") int size) {

        return null;
    }

    /**
     * 删除用户，通过用户的ID
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        return null;
    }

    @PutMapping("")
    public ResponseResult updateUser() {
        return null;
    }

}
