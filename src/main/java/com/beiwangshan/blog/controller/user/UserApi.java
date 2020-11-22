package com.beiwangshan.blog.controller.user;

import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.RedisUtils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private RedisUtils redisUtils;

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
    public ResponseResult register(@RequestBody BwsUser bwsUser,
                                   @RequestParam("email_code") String emailCode,
                                   @RequestParam("captcha_code") String captchaCode,
                                   @RequestParam("captcha_key") String captchaKey,
                                   HttpServletRequest request
    ) {
        return userService.register(bwsUser, emailCode, captchaCode, captchaKey, request);
    }

    /**
     * 用户登录 captcha 图灵验证码
     * 需要提交的数据：
     * 1、用户账号 ==> 用户名+用户邮箱 ==> 做了唯一的处理
     * 2、用户密码
     * 3、图灵验证码
     * 4、图灵验证码的key
     *
     * @param captcha 图灵验证码
     * @param bwsUser 用户的bean类，封装着账号和密码
     * @return
     * @Param captchaKey 图灵验证码的key
     */
    @PostMapping("/{captcha}/{captcha_key}")
    public ResponseResult login(@PathVariable("captcha") String captcha,
                                @PathVariable("captcha_key") String captchaKey,
                                @RequestBody BwsUser bwsUser,
                                HttpServletRequest request,
                                HttpServletResponse response
    ) {
        return userService.doLogin(captcha, captchaKey, bwsUser, request, response);
    }


    /**
     * 获取图灵验证码
     * 有效时长是 10 分钟
     * 可以在 redisUtil.set(Contants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10); 中更改
     *
     * @return
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchaKey) throws Exception {
        try {
            userService.createCaptcha(response, captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }


    /**
     * 发送邮件
     * <p>
     * 业务场景：
     * 注册，找回密码，修改邮箱（新的邮箱）
     * <p>
     * 注册：如果已经存在，提示该邮箱已经注册
     * 找回密码：如果没有注册，提示该邮箱没有注册
     * 修改邮箱：（新的邮箱）如果已经注册了，提示该邮箱已经注册
     *
     * @param emailAddress
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(HttpServletRequest request,
                                         @RequestParam("type") String type,
                                         @RequestParam("email") String emailAddress
    ) {
        log.info("emailAddress ==> " + emailAddress);
        return userService.sendEmail(type, request, emailAddress);
    }

    /**
     * 修改密码
     *  1.修改密码
     *      普通做法：通过旧密码对比来更新密码
     *  2.找回密码，即可找回密码，也可以修改密码
     *      发送验证码到邮箱或者手机 ==> 判断验证码是否正确 ==> 判断对应的邮箱或者手机号所注册的账号是否属于用户
     *  步骤：
     *      1.用户填写邮箱
     *      2.用户获取验证码 type = forget ==> LOGIN_TYPE_FORGET
     *      3.用户填写验证码
     *      4.填写新的密码
     *      5.提交数据
     *
     *   数据包括：
     *      1.邮箱和新密码
     *      2.验证码
     *          如果验证码正确，说明所用邮箱注册的账号属于用户，就可以修改密码
     *
     * @param bwsUser
     * @return
     */
    @PutMapping("/password/{verify_code}")
    public ResponseResult updatePassword(@PathVariable("verify_code")String verifyCode,
                                         @RequestBody BwsUser bwsUser) {

        return userService.updatePassword(verifyCode,bwsUser);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {

        return userService.getUserInfo(userId);
    }

    /**
     * 更新用户信息 user-info
     * 校验用户权限 HttpServletResponse response
     * 1.允许用户修改密码
     * 2.用户头像
     * 3.用户名 （唯一的）
     * 4.密码 （单独修改，需要验证）
     * 5.签名
     * 6.email （唯一的，单独修改，需要验证）
     *
     * @param userId
     * @param bwsUser
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo( HttpServletRequest request,
                                          HttpServletResponse response,
                                         @PathVariable("userId") String userId,
                                         @RequestBody BwsUser bwsUser) {

        return userService.updateUserInfo(request,response,userId, bwsUser);
    }

    /**
     * 获取用户列表
     *  需要管理员权限
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listUser(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                   ) {

        return userService.listUser(page,size,request,response);
    }

    /**
     * 删除用户，通过用户的ID
     *  需要管理员的权限
     *      判断当前操作的用户是谁
     *      根据用户的角色判断是否可以执行下一步操作
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(HttpServletResponse response,
                                     HttpServletRequest request,
                                     @PathVariable("userId") String userId) {
        //TODO:通过注解的方式来控制权限

        return userService.deleteUserById(userId,response,request);
    }


    /**
     * 检查该email 是否已经注册了
     *
     * @param email
     * @return SUCCESS --> 已经注册 FAILED ---> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示当前邮箱已经注册"),
            @ApiResponse(code = 40000, message = "表示当前邮箱未注册"),

    })
    @PutMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }


    /**
     * 检查该userName 是否已经注册了
     *
     * @param userName
     * @return SUCCESS --> 已经注册 FAILED ---> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示当前用户名已经注册"),
            @ApiResponse(code = 40000, message = "表示当前用户名未注册"),

    })
    @PutMapping("/user_name")
    public ResponseResult checkUserName(@RequestParam("userName") String userName) {
        return userService.checkUserName(userName);
    }

}
