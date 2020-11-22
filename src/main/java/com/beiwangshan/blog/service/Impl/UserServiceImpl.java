package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.RefreshTokenDao;
import com.beiwangshan.blog.dao.SettingsDao;
import com.beiwangshan.blog.dao.UserDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.RefreshToken;
import com.beiwangshan.blog.pojo.Setting;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.response.ResponseState;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.service.TaskService;
import com.beiwangshan.blog.utils.*;
import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
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

    /**
     * 引入雪花算法，计算ID
     */
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * BCryptPasswordEncoder密码验证
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 注入随机数
     */
    @Autowired
    private Random random;

    /**
     * 注入redis工具类
     */
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SettingsDao settingsDao;

    /**
     * 注入
     */
    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Autowired
    private Gson gson;

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
            return ResponseResult.FAILED("管理员账号已经初始化了");
        }

//        检查数据 用户名，邮箱，密码是否为空
        if (TextUtils.isEmpty(bwsUser.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getEmail())) {
            return ResponseResult.FAILED("Email不能为空");
        }

//        补充数据
        /**
         * 雪花算法来生成ID SnowFlakeIdWorker
         */
        bwsUser.setId(String.valueOf(snowflakeIdWorker.nextId()));
        bwsUser.setRoles(Constants.User.ROLE_ADMIN);
        bwsUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        bwsUser.setState(Constants.User.DEFAULT_STATE);
//        获取IP 本地ip
        String localAddr = request.getLocalAddr();
        //代理ip
        String remoteAddr = request.getRemoteAddr();
        bwsUser.setLoginIp(remoteAddr);
        bwsUser.setRegIp(remoteAddr);
        bwsUser.setCreateTime(new Date());
        bwsUser.setUpdateTime(new Date());

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
        //1表示存在，0表示删除
        setting.setValue("1");
        settingsDao.save(setting);


        return ResponseResult.SUCCESS("初始化成功");
    }

    /**
     * 图灵验证码的字体设置 数组形式，保证请求的不一致性
     * 防止机器攻击注册
     */
    public static final int[] CAPTCHA_FONT_TYPES = {
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
            // 几位数运算，默认是两位
            tagetCaptcha.setLen(2);
            tagetCaptcha.text();  // 获取运算的结果：
        }

        // 设置字体
        int fontTypeIndex = random.nextInt(CAPTCHA_FONT_TYPES.length);
        log.info("fontTypeIndex ==> " + String.valueOf(fontTypeIndex));
        tagetCaptcha.setFont(CAPTCHA_FONT_TYPES[fontTypeIndex]);
        // 设置类型，纯数字、纯字母、字母数字混合
        tagetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

//        获取内容
        String content = tagetCaptcha.text().toLowerCase();
        log.info("图灵验证码 ==> " + content);

//        验证码信息 保存在redis里
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, Constants.TimeValueInMillions.MIN_10);

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
            return ResponseResult.FAILED("邮箱地址不能为空");
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
        if (Constants.User.LOGIN_TYPE_REGISTER.equals(type) || Constants.User.LOGIN_TYPE_UPDATE.equals(type)) {
            BwsUser userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已经被注册");
            }
        } else if (Constants.User.LOGIN_TYPE_FORGET.equals(type)) {
            BwsUser userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail == null) {
                return ResponseResult.FAILED("该邮箱未注册");
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
            return ResponseResult.FAILED("发送过于频繁，请稍后再试！");
        }

//        获取邮箱验证码的发送次数，同理
        Object hasEmailSend = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("发送过于频繁，请稍后再试！");
        }

//        2.检查邮箱是否正确
        boolean isEmailFormatOk = TextUtils.isEmailAddressOk(emailAddress);
        if (!isEmailFormatOk) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
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
            return ResponseResult.FAILED("验证码发送异常，请稍后重试");
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
        log.info("邮箱验证码===>" + code);
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
    public ResponseResult register(BwsUser bwsUser,
                                   String emailCode,
                                   String captchaCode,
                                   String captchaKey,
                                   HttpServletRequest request) {
//        1.检查当前用户是否已经注册
        String userName = bwsUser.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不能为空");
        }

        BwsUser userFromDbByUserName = userDao.findOneByUserName(userName);
        if (userFromDbByUserName != null) {
            return ResponseResult.FAILED("该用户已经注册");
        }

//        2.检查邮箱格式是否正确（前端+后端都可以的）
        String emailAddr = bwsUser.getEmail();
        if (emailAddr.isEmpty()) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }
        if (!TextUtils.isEmailAddressOk(emailAddr)) {
            return ResponseResult.FAILED("邮箱地址不正确");
        }

//        3.检查邮箱是否已经注册
        BwsUser userFromDbByEmail = userDao.findOneByEmail(emailAddr);
        if (userFromDbByEmail != null) {
            return ResponseResult.FAILED("该邮箱地址已经注册");
        }

//        4.检查邮箱验证码是都正确
        String emailVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
        log.info("拿到的 emailVerifyCode ===> " + emailVerifyCode);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码无效");
        }

        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        } else {
//            验证码正确，干掉redis里面的内容
//            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
        }

//        5.检查图灵验证码是否正确 1.拿到验证码
        String captchaVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);

        log.info("拿到的captchaVerifyCode ==>" + captchaVerifyCode);
        log.info("拿到的captchaKey ==>" + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已经过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码不正确");
        } else {
        }

        if (captchaVerifyCode.equals(captchaCode) && emailVerifyCode.equals(emailCode)) {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);

        }

//        达到注册的条件  ps:前端可以对用户名等进行校验
//        6.对密码进行加密
        String password = bwsUser.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不能为空");
        }
        bwsUser.setPassword(bCryptPasswordEncoder.encode(bwsUser.getPassword()));

//        7.补全数据 包括ip，角色，创建时间，更新时间
        String ipAddr = request.getRemoteAddr();
        bwsUser.setRegIp(ipAddr);
        bwsUser.setLoginIp(ipAddr);
        bwsUser.setCreateTime(new Date());
        bwsUser.setUpdateTime(new Date());
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
            return ResponseResult.FAILED("验证码不能为空");
        }
        if (TextUtils.isEmpty(bwsUser.getEmail()) && TextUtils.isEmpty(bwsUser.getUserName())) {
            return ResponseResult.FAILED("账户名不能为空");
        }


        if (TextUtils.isEmpty(bwsUser.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }

        //获取redis中保存的 图灵验证码的信息
        String captchaFromRedis = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        log.info("获取到的人类验证码" + captchaFromRedis);
        log.info("传入的人类验证码" + captcha);
        //进行判断 是否和携带的图灵验证码是否一致
        if (!captcha.equals(captchaFromRedis)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }

        //根据传入的数据进行查询是否存在这个用户
        BwsUser userFromDb = userDao.findOneByUserName(bwsUser.getUserName());
        if (userFromDb == null) {
            userFromDb = userDao.findOneByEmail(bwsUser.getEmail());
        }
        if (userFromDb == null) {
            return ResponseResult.FAILED("用户名或密码错误");
        }

        //用户存在，对比密码
        boolean matches = bCryptPasswordEncoder.matches(bwsUser.getPassword(), userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("用户名或密码错误");
        }
        //密码是正确的
        //判断用户状态，如果是非正常状态，则返回结果
        if ("0".equals(userFromDb.getState())) {
            return ResponseResult.ACCOUNT_DENIAL();
        }
        createToken(response, userFromDb);
        return ResponseResult.GET_STATE(ResponseState.LOGIN_SUCCESS);
    }

    /**
     * 返回token_key
     *
     * @param response
     * @param userFromDb
     * @return token_key
     */
    private String createToken(HttpServletResponse response, BwsUser userFromDb) {
        int deleteResult = refreshTokenDao.deleteAllByUserId(userFromDb.getId());
        log.info("deleteResult == > " + deleteResult);
        //生成token
        Map<String, Object> cliams = ClaimsUtils.bwsUser2Claims(userFromDb);

        //生成 token 默认有效期是两个小时
        String token = JwtUtil.createToken(cliams);

        // 返回token的md5值，token会保存在redis里面，前端访问的时候，携带token的md5 key
        //从redis中，获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token 到 redis里，有效期是两个小时，key是tokenKey
        redisUtil.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValueInMillions.HOUR_2);
        //创建一个cookies
        Cookie cookie = new Cookie(Constants.User.COOKIE_TOKEN_KEY, tokenKey);
        //需要动态获取，可以从request里获取
        //工具类实现
        cookie.setDomain("localhost");
        cookie.setPath("/");
        //设置时间,这里默认设置的是一个月
        cookie.setMaxAge(Constants.TimeValueInMillions.MONTH);
        // 把 tokenKey 写到 cookies里
        response.addCookie(cookie);

        // 生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.TimeValueInMillions.MONTH);
        //保存到数据库
        /**
         * refreshToken，tokenKey,用户ID，创建时间，更新时间
         */
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(snowflakeIdWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(userFromDb.getId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());

        this.refreshTokenDao.save(refreshToken);

        return tokenKey;
    }

    /**
     * 检查用户登录状态
     * 本质就是通过携带的 token_key检查用户是否有登录，如果有登录，就返回用户信息，如果没有就提示
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public BwsUser checkBwsUser(HttpServletRequest request, HttpServletResponse response) {
        //1.拿到token_key
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        log.info("检查登录时候拿到的tokenKey===>" + tokenKey);
        // 从redis中取得 token
        String redisToken = (String) redisUtil.get(Constants.User.KEY_TOKEN+tokenKey);
        log.info("checkBwsUser ==> redisToken==> "+redisToken);
        BwsUser bwsUser = parseByTokenKey(tokenKey);
        log.info("检查登录时候拿到的tokenKey 解析后的 user===>" + bwsUser);
        if (bwsUser == null) {
            // 说明解析出错，过期了，
            // - 去数据库查询，根据 refrToken，
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            // - 如果不存在，就是没登录
            if (refreshToken == null) {
                log.info("checkBwsUser  refreshToken ==> 为空");
                return null;
            }
            // - 如果存在就解析 refrToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                // - 如果有效，就创建新的token，并更新 refrToken
                String userId = refreshToken.getUserId();
                BwsUser bwsUserById = userDao.findOneById(userId);
                // 删掉之前的 refreshToken 记录

                // 创建新的，并且存入数据库
                String newTokenKey = createToken(response, bwsUserById);
                log.info("checkBwsUser 创建新的 refreshToken ==> " + newTokenKey);
                // 返回 token
                return parseByTokenKey(newTokenKey);
            } catch (Exception exception) {
                // - 如果 refrToken 过期了，就返回 当前用户没有登录
                log.info("checkBwsUser  refreshToken ==> 过期");
                return null;
            }
        }
        return bwsUser;
    }

    /**
     * 获取用户的信息
     * 1.从数据库里获取
     * 2.判断结果
     * - 如果不存在，就返回不存在
     * - 如果存在，就复制对象，清空携带的密码，email，注册和登录的IP
     * 3.返回结果
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserInfo(String userId) {
//        查询用户信息从数据库
        BwsUser bwsUser = userDao.findOneById(userId);
//        判断用户是否存在
        if (bwsUser == null) {
//            用户不存在，返回查询失败
            return ResponseResult.FAILED("用户不存在");
        }
//        用户存在，清空敏感数据
        String userJson = gson.toJson(bwsUser);
        BwsUser newBwsUser = gson.fromJson(userJson, BwsUser.class);
        newBwsUser.setPassword("");
        newBwsUser.setLoginIp("");
        newBwsUser.setRegIp("");
        newBwsUser.setEmail("");

//        读取用户信息完成，并返回用户的信息
        return ResponseResult.SUCCESS("读取用户信息成功").setData(newBwsUser);
    }

    /**
     * 用户在修改用户信息之前验证 email
     * 保证email的唯一性
     *
     * @param email
     * @return
     */
    @Override
    public ResponseResult checkEmail(String email) {
        BwsUser oneByEmail = userDao.findOneByEmail(email);
        return oneByEmail==null?ResponseResult.FAILED("该邮箱未注册"):ResponseResult.SUCCESS("该邮箱已注册");
    }

    /**
     * 检查用户名是否已经注册，保证其唯一性
     *
     * @param userName
     * @return
     */
    @Override
    public ResponseResult checkUserName(String userName) {
        BwsUser oneByUserName = userDao.findOneByUserName(userName);
        return oneByUserName==null?ResponseResult.FAILED("该用户名未注册"):ResponseResult.SUCCESS("该用户名已注册");
    }

    /**
     * 更新用户信息
     *
     * @param request  检查用户登录状态
     * @param response 检查用户登录状态
     * @param userId   查询
     * @param bwsUser  查询
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(HttpServletRequest request, HttpServletResponse response, String userId, BwsUser bwsUser) {
        //检查用户的登录状态 从token里面解析出来的，为了校验权限，只有用户才可以操作
        BwsUser userFromTokenKey = checkBwsUser(request, response);
        if (userFromTokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        用户已经登录 判断当前用户的ID 和即将修改的用户ID 是否一致，一致才可以修改
        BwsUser userFromDb = userDao.findOneById(userFromTokenKey.getId());
        if (!userFromDb.getId().equals(userId)){
            return ResponseResult.PERMISSION_DENIAL();
        }
//        Id一致，可以修改，可以修改的项目：头像，用户名，签名

        //        用户名不能为空
        String userName =bwsUser.getUserName();
        if (!TextUtils.isEmpty(userName)) {
//            检查是否已经存在
            BwsUser oneByUserName = userDao.findOneByUserName(userName);
            if (oneByUserName != null) {
                return ResponseResult.FAILED("该用户名已经注册");
            }
            userFromDb.setUserName(userName);
        }

//        头像不能为空
        if (!TextUtils.isEmpty(bwsUser.getAvatar())) {
            userFromDb.setAvatar(bwsUser.getAvatar());
        }

//        签名可以为空，可以直接设置
        userFromDb.setSign(bwsUser.getSign());

//        干掉redis里面的token，下一次请求的时候，就需要解析token，就会根据refreshToken重新创建一个
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        redisUtil.del(Constants.User.KEY_TOKEN+tokenKey);

        userDao.save(userFromDb);

        return ResponseResult.SUCCESS("用户信息更新完成");
    }

    /**
     * 通过userId来删除用户，在此之前需要判断操作用户的权限
     *  并不是真的删除，而是修改状态，需要管理员权限
     *
     * @param userId
     * @param response
     * @param request
     * @return
     */
    @Override
    public ResponseResult deleteUserById(String userId, HttpServletResponse response, HttpServletRequest request) {
//        检验当前用户是谁
        BwsUser currentUser = checkBwsUser(request, response);
        if (currentUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        已经登录，判断权限
        log.info("删除时候的用户权限：==>"+currentUser.getUserName()+"==>"+currentUser.getRoles());
        if (!Constants.User.ROLE_ADMIN.equals(currentUser.getRoles())){
            return ResponseResult.PERMISSION_DENIAL();
        }
//        可以操作
        int delResult = userDao.deleteUserByState(userId);
        if (delResult>0) {
            return ResponseResult.SUCCESS("删除成功");
        }

        return ResponseResult.FAILED("用户不存在，删除失败");
    }


    /**
     * 解析 token 通过token_key
     * @param tokenKey
     * @return BwsUser
     */
    private BwsUser parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(Constants.User.KEY_TOKEN+tokenKey);
        log.info("parseByTokenKey ==> token ===>" + token);
        if (token != null) {
            try {
                //解析token
                Claims claims = JwtUtil.parseJWT(token);
                //解析token为 User 实体类
                return ClaimsUtils.cliams2BwsUser(claims);
            } catch (Exception e) {
                log.info("parseByTokenKey  ==> " + token + "过期了");
                return null;
            }
        }
        return null;

    }
}
