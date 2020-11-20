package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.SettingsDao;
import com.beiwangshan.blog.dao.UserDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Setting;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.response.ResponseState;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.service.TaskService;
import com.beiwangshan.blog.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @className: com.beiwangshan.blog.service.Impl-> UserServiceImpl
 * @description: 初始化管理员账号的实现类
 * @author: 曾豪
 * @createDate: 2020-11-18 0:30
 * @version: 1.0
 * @todo: Transactional 用来操作数据库
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {

    //    引入雪花算法，计算ID
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    //    BCryptPasswordEncoder密码验证
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Random random;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SettingsDao settingsDao;

    /**
     * 新增管理员
     *
     * @param bwsUser
     * @param request
     * @return
     */
    @Override
    public ResponseResult initManagerAccount(BwsUser bwsUser, HttpServletRequest request) {
//        检查是否初始化
        Setting managerAccountState = settingsDao.findOneByKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        if (managerAccountState != null) {
            return ResponseResult.FAILD("管理员账号已经初始化了");
        }

//        检查数据 用户名，邮箱，密码是否为空
        if (TextUtils.isEmpty(bwsUser.getUserName())) {
            return ResponseResult.FAILD("用户名不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getPassword())) {
            return ResponseResult.FAILD("密码不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getEmail())) {
            return ResponseResult.FAILD("Email不能为空");
        }

//        补充数据
        /**
         * 雪花算法来生成ID SnowFlakeIdWorker
         */
        bwsUser.setId(String.valueOf(snowflakeIdWorker.nextId()));
        bwsUser.setRoles(Constants.User.ROLE_ADMIN);
        bwsUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        bwsUser.setState(Constants.User.DEFAULT_STATE);
//        获取IP
        String localAddr = request.getLocalAddr();//本地ip
        String remoteAddr = request.getRemoteAddr();//代理ip
        bwsUser.setLogin_ip(remoteAddr);
        bwsUser.setReg_ip(remoteAddr);
        bwsUser.setCreateTime(new Date());
        bwsUser.setUpdate_time(new Date());

//        对密码进行加密
//        原密码
        String password = bwsUser.getPassword();
//        加密
        String encode = bCryptPasswordEncoder.encode(password);
        bwsUser.setPassword(encode);

//        保存到数据库
        userDao.save(bwsUser);

//        更新已经添加的标记
//        肯定没有的
        Setting setting = new Setting();
        setting.setId(String.valueOf(snowflakeIdWorker.nextId()));
        setting.setKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        setting.setCreateTime(new Date());
        setting.setUpdateTime(new Date());
        setting.setValue("1");//1表示存在，0表示删除
        settingsDao.save(setting);


        return ResponseResult.SUCCESS("初始化成功");
    }

    /**
     * 图灵验证码的字体设置 数组形式，保证请求的不一致性
     * 防止机器攻击注册
     */
    public static final int[] captcha_font_types = {
            Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10
    };

    /**
     * 图灵验证码
     *
     * @param response
     * @param captchaKey
     * @throws Exception
     */
    @Override
    public void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception {
        //        判断key是否符合规则
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }

//        开始图灵验证码
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

//        生成随机数，完成随机产生验证码的类型
        int captchaType = random.nextInt(3);
        Captcha tagetCaptcha;
        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            tagetCaptcha = new SpecCaptcha(200, 60, 5);
        } else if (captchaType == 1) {
            // gif类型
            tagetCaptcha = new GifCaptcha(200, 60);
        } else {
            // 算术类型
            tagetCaptcha = new ArithmeticCaptcha(200, 60);
            tagetCaptcha.setLen(2);  // 几位数运算，默认是两位
            tagetCaptcha.text();  // 获取运算的结果：
        }

        // 设置字体
        int fontTypeIndex = random.nextInt(captcha_font_types.length);
        log.info("fontTypeIndex ==> " + String.valueOf(fontTypeIndex));
        tagetCaptcha.setFont(captcha_font_types[fontTypeIndex]);
        // 设置类型，纯数字、纯字母、字母数字混合
        tagetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

//        获取内容
        String content = tagetCaptcha.text().toLowerCase();
        log.info("图灵验证码 ==> " + content);

//        验证码信息 保存在redis里
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);

        // 输出图片流
        tagetCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /**
     * 发送邮件验证码
     * <p>
     * // 检查邮箱格式，判空
     * let reg = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
     * if (!reg.test(邮箱地址)) {
     * consol.log('邮箱地址格式不对..');
     * return
     * }
     *
     * @param type
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress) {

        if (emailAddress == null) {
            return ResponseResult.FAILD("邮箱地址不能为空");
        }

        /**
         *      业务场景：
         *           注册，找回密码，修改邮箱（新的邮箱）
         *
         *        注册(register)：如果已经存在，提示该邮箱已经注册
         *        找回密码(forget)：如果没有注册，提示该邮箱没有注册
         *        修改邮箱(update)：（新的邮箱）如果已经注册了，提示该邮箱已经注册
         */
//        根据类型，查询邮箱是否存在
        if ("register".equals(type) || "update".equals(type)) {
            BwsUser userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILD("该邮箱已经被注册");
            }
        } else if ("forget".equals(type)) {
            BwsUser userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail == null) {
                return ResponseResult.FAILD("该邮箱未注册");
            }
        }

        String remoteAdress = request.getRemoteAddr();
        log.info("sendEmail ==> ip ==> " + remoteAdress);

//        转换 ip 地址的格式
        if (remoteAdress == null) {
            remoteAdress = remoteAdress.replaceAll(":", "_");
        }


        //1.防止暴力发送，就是不断的发送，同一个邮箱，间隔需要超过1分钟，一小时内同一个IP，最多只能10次，短信最多是 3 次
//        获取邮箱发送IP的次数，如果没有 ==> 继续；如果有 ==> 判断次数 ==> 操作
        Integer ipSendTimes = (Integer) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAdress);

        if (ipSendTimes != null && ipSendTimes > 10) {
            return ResponseResult.FAILD("发送过于频繁，请稍后再试！");
        }

//        获取邮箱验证码的发送次数，同理
        Object hasEmailSend = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILD("发送过于频繁，请稍后再试！");
        }

//        2.检查邮箱是否正确
        boolean isEmailFormatOk = TextUtils.isEmailAddressOk(emailAddress);
        if (!isEmailFormatOk) {
            return ResponseResult.FAILD("邮箱地址格式不正确");
        }

//        3.生成随机的6位数验证码
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }

//        3.发送验证码 100000~999999
        try {
            taskService.SendEmailVerifyCode(String.valueOf(code), emailAddress);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseResult.FAILD("验证码发送异常，请稍后重试");
        }

//        4.做记录 ==> 发送记录和code
        if (ipSendTimes == null) {
            ipSendTimes = 0;
        }

//        设置IP 和 邮箱地址 一小时有效期 和 30 秒
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAdress, ipSendTimes, 60 * 60);
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
//        保存code，10分钟内有效
        redisUtil.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
        return ResponseResult.SUCCESS("验证码发送成功");
    }


    /**
     * 用户注册
     *
     * @param bwsUser     传入的实体类
     * @param emailCode   邮箱的验证码
     * @param captchaCode 人类验证码
     * @param captchaKey  携带的key
     * @param request     传入HTTP，获取IP
     * @return
     */
    @Override
    public ResponseResult register(BwsUser bwsUser, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request) {
//        1.检查当前用户是否已经注册
        String userName = bwsUser.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILD("用户名不能为空");
        }

        BwsUser userFromDbByUserName = userDao.findOneByUserName(userName);
        if (userFromDbByUserName != null) {
            return ResponseResult.FAILD("该用户已经注册");
        }

//        2.检查邮箱格式是否正确（前端+后端都可以的）
        String emailAddr = bwsUser.getEmail();
        if (emailAddr.isEmpty()) {
            return ResponseResult.FAILD("邮箱地址不可以为空");
        }
        if (!TextUtils.isEmailAddressOk(emailAddr)) {
            return ResponseResult.FAILD("邮箱地址不正确");
        }

//        3.检查邮箱是否已经注册
        BwsUser userFromDbByEmail = userDao.findOneByEmail(emailAddr);
        if (userFromDbByEmail != null) {
            return ResponseResult.FAILD("该邮箱地址已经注册");
        }

//        4.检查邮箱验证码是都正确
        String emailVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILD("验证码已过期");
        }

        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILD("邮箱验证码不正确");
        } else {
//            验证码正确，干掉redis里面的内容
            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
        }

//        5.检查图灵验证码是否正确
//              1.拿到验证码
        String captchaVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILD("人类验证码已经过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILD("人类验证码不正确");
        } else {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }

//        达到注册的条件  ps:前端可以对用户名等进行校验
//        6.对密码进行加密
        String password = bwsUser.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILD("密码不能为空");
        }
        bwsUser.setPassword(bCryptPasswordEncoder.encode(password));

//        7.补全数据 包括ip，角色，创建时间，更新时间
        String ipAddr = request.getRemoteAddr();
        bwsUser.setReg_ip(ipAddr);
        bwsUser.setCreateTime(new Date());
        bwsUser.setUpdate_time(new Date());
        bwsUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        bwsUser.setRoles(Constants.User.ROLE_NORMAL);
        bwsUser.setId(String.valueOf(snowflakeIdWorker.nextId()));
        bwsUser.setState("1");

//        8.保存到数据库
        userDao.save(bwsUser);

//        9.返回结果
        return ResponseResult.GET_STATE(ResponseState.JOIN_IN_SUCCESS);
    }

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
    @Override
    public ResponseResult doLogin(String captcha,
                                  String captchaKey,
                                  BwsUser bwsUser,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        /**
         *  用户登录 captcha 图灵验证码
         *    需要提交的数据：
         *       1、用户账号 ==> 用户名+用户邮箱 ==> 做了唯一的处理
         *       2、用户密码
         *       3、图灵验证码
         *       4、图灵验证码的key
         */

        //对数据进行判空处理
        if (captcha == "" || captcha == null) {
            return ResponseResult.FAILD("验证码不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getEmail())) {
            return ResponseResult.FAILD("邮箱不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getUserName())) {
            return ResponseResult.FAILD("用户名不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getPassword())) {
            return ResponseResult.FAILD("密码不能为空");
        }

        //获取redis中保存的 图灵验证码的信息
        String captchaFromRedis = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        //进行判断 是否和携带的图灵验证码是否一致
        if (!captcha.equals(captchaFromRedis)) {
            return ResponseResult.FAILD("验证码不正确");
        }

        //根据传入的数据进行查询是否存在这个用户 
        BwsUser userFromDb = userDao.findOneByUserName(bwsUser.getUserName());
        if (userFromDb == null) {
            userFromDb = userDao.findOneByEmail(bwsUser.getEmail());
        }
        if (userFromDb == null) {
            return ResponseResult.FAILD("用户名或密码错误");
        }

        //用户存在，对比密码
        boolean matches = bCryptPasswordEncoder.matches(bwsUser.getPassword(), userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.FAILD("用户名或密码错误");
        }
        //密码是正确的
        //判断用户状态，如果是非正常状态，则返回结果
        if ("1".equals(userFromDb.getState())) {
            return ResponseResult.FAILD("该账户已被禁止");
        }
        //TODO:生成token
        Map<String, Object> cliams = new HashMap<>();
        //放入数据
        cliams.put("id", userFromDb.getId());
        cliams.put("userName", userFromDb.getUserName());
        cliams.put("roles", userFromDb.getRoles());
        cliams.put("avatar", userFromDb.getAvatar());
        cliams.put("email", userFromDb.getEmail());
        cliams.put("sign", userFromDb.getSign());

        //生成 token 默认有效期是两个小时
        String token = JwtUtil.createToken(cliams);

        // 返回token的md5值，token会保存在redis里面，前端访问的时候，携带token的md5 key
        //从redis中，获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token 到 redis里，有效期是两个小时，key是tokenKey
        redisUtil.set(Constants.User.KEY_TOKEN+tokenKey,token,60*60*2);
        //创建一个cookies
        Cookie cookie = new Cookie("bws_log_token",tokenKey);
        //需要动态获取，可以从request里获取
        //TODO:工具类实现
        cookie.setDomain("localhost");
        cookie.setPath("/");
        //设置时间,这里默认设置的是一个月
        cookie.setMaxAge(Constants.User.TOKEN_MAX_AGE);
        // 把 tokenKey 写到 cookies里
        response.addCookie(cookie);


        // TODO:生成refreshToken

        return ResponseResult.GET_STATE(ResponseState.LOGIN_SUCCESS);
    }


}
