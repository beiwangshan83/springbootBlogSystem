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
import com.beiwangshan.blog.utils.*;
import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    private RedisUtils redisUtils;

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
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < Constants.User.CAPTCHA_KEY_LENGTH) {
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
        Captcha targetCaptcha;
        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            targetCaptcha = new SpecCaptcha(200, 60, 5);
        } else if (captchaType == 1) {
            // gif类型
            targetCaptcha = new GifCaptcha(200, 60);
        } else {
            // 算术类型
            targetCaptcha = new ArithmeticCaptcha(200, 60);
            // 几位数运算，默认是两位
            targetCaptcha.setLen(2);
            targetCaptcha.text();  // 获取运算的结果：
        }

        // 设置字体
        int fontTypeIndex = random.nextInt(CAPTCHA_FONT_TYPES.length);
        targetCaptcha.setFont(CAPTCHA_FONT_TYPES[fontTypeIndex]);
        // 设置类型，纯数字、纯字母、字母数字混合
        targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

//        获取内容
        String content = targetCaptcha.text().toLowerCase();
        log.info("图灵验证码 ==> " + content);

//        验证码信息 保存在redis里
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, Constants.TimeValueInMillions.MIN_10);

        // 输出图片流
        targetCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /**
     * 发送邮件验证码
     * <p>
     * // 检查邮箱格式，判空
     * let reg = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
     * if (!reg.test(邮箱地址)) {
     * console.log('邮箱地址格式不对..');
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

        String remoteAddress = request.getRemoteAddr();

//        转换 ip 地址的格式
        if (remoteAddress == null) {
            remoteAddress = remoteAddress.replaceAll(":", "_");
        }


        //1.防止暴力发送，就是不断的发送，同一个邮箱，间隔需要超过1分钟，一小时内同一个IP，最多只能10次，短信最多是 3 次
//        获取邮箱发送IP的次数，如果没有 ==> 继续；如果有 ==> 判断次数 ==> 操作
        Integer ipSendTimes = (Integer) redisUtils.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddress);

        if (ipSendTimes != null && ipSendTimes > 10) {
            return ResponseResult.FAILED("发送过于频繁，请稍后再试！");
        }

//        获取邮箱验证码的发送次数，同理
        Object hasEmailSend = redisUtils.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
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
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddress, ipSendTimes, 60 * 60);
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
//        保存code，10分钟内有效
        redisUtils.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
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
        String emailVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码无效");
        }

        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        }else{
            redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
//        5.检查图灵验证码是否正确 拿到验证码
        String captchaVerifyCode = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);

        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已经过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码不正确");
        } else {
            redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddr);
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
        String captchaFromRedis = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        //进行判断 是否和携带的图灵验证码是否一致
        if (!captcha.equals(captchaFromRedis)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
//        图灵验证码正确，删除它
        redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);

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
        if (Constants.User.DENIAL_STATE.equals(userFromDb.getState())) {
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
        //生成token
        Map<String, Object> claims = ClaimsUtils.bwsUser2Claims(userFromDb);

        //生成 token 默认有效期是两个小时
        String token = JwtUtils.createToken(claims);

        // 返回token的md5值，token会保存在redis里面，前端访问的时候，携带token的md5 key
        //从redis中，获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token 到 redis里，有效期是两个小时，key是tokenKey
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValueInMillions.HOUR_2);
        //创建一个cookies
        Cookie cookie = new Cookie(Constants.User.COOKIE_TOKEN_KEY, tokenKey);
        //需要动态获取，可以从request里获取
        //工具类实现
        cookie.setDomain("localhost");
        cookie.setPath("/");
        //设置时间,这里默认设置的是一个月
        cookie.setMaxAge(Constants.TimeValueInSecond.MONTH);
        // 把 tokenKey 写到 cookies里
        response.addCookie(cookie);

        // 生成refreshToken
        String refreshTokenValue = JwtUtils.createRefreshToken(userFromDb.getId(), Constants.TimeValueInSecond.MONTH);
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
     * @return
     */
    @Override
    public BwsUser checkBwsUser() {

        //1.拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        // 从redis中取得 token
        String redisToken = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        BwsUser bwsUser = parseByTokenKey(tokenKey);
        if (bwsUser == null) {
            // 说明解析出错，过期了，
            // - 去数据库查询，根据 refreshToken，
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            // - 如果不存在，就是没登录
            if (refreshToken == null) {
                return null;
            }
            // - 如果存在就解析 refreshToken
            try {
                JwtUtils.parseJWT(refreshToken.getRefreshToken());
                // - 如果有效，就创建新的token，并更新 refreshToken
                String userId = refreshToken.getUserId();
                BwsUser bwsUserById = userDao.findOneById(userId);
                // 删掉之前的 refreshToken 记录

                // 创建新的，并且存入数据库
                String newTokenKey = createToken(getResponse(), bwsUserById);
                // 返回 token
                return parseByTokenKey(newTokenKey);
            } catch (Exception exception) {
                // - 如果 refreshToken 过期了，就返回 当前用户没有登录
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
        return oneByEmail == null ? ResponseResult.FAILED("该邮箱未注册") : ResponseResult.SUCCESS("该邮箱已注册");
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
        return oneByUserName == null ? ResponseResult.FAILED("该用户名未注册") : ResponseResult.SUCCESS("该用户名已注册");
    }

    /**
     * 更新用户信息
     *
     * @param userId  查询
     * @param bwsUser 查询
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(String userId, BwsUser bwsUser) {
        //检查用户的登录状态 从token里面解析出来的，为了校验权限，只有用户才可以操作
        BwsUser userFromTokenKey = checkBwsUser();
        if (userFromTokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        用户已经登录 判断当前用户的ID 和即将修改的用户ID 是否一致，一致才可以修改
        BwsUser userFromDb = userDao.findOneById(userFromTokenKey.getId());
        if (!userFromDb.getId().equals(userId)) {
            return ResponseResult.PERMISSION_DENIAL();
        }
//        Id一致，可以修改，可以修改的项目：头像，用户名，签名

        //        用户名不能为空
        String userName = bwsUser.getUserName();
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
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);

        userDao.save(userFromDb);

        return ResponseResult.SUCCESS("用户信息更新完成");
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    /**
     * 通过userId来删除用户，在此之前需要判断操作用户的权限
     * 并不是真的删除，而是修改状态，需要管理员权限
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult deleteUserById(String userId) {
//        可以操作
        int delResult = userDao.deleteUserByState(userId);
        if (delResult > 0) {
            return ResponseResult.SUCCESS("删除成功");
        }

        return ResponseResult.FAILED("用户不存在，删除失败");
    }

    /**
     * 查询用户列表
     * 需要管理员权限，分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listUser(int page, int size) {
//        判断page大小，分页查询
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }

        if (size < Constants.Page.MIN_SIZE) {
            size = Constants.Page.MIN_SIZE;
        }

        //分页查询，根据注册日期来排序
        //TODO:实现查询所有用户信息，但是不查询到用户密码
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
//       TODO: 就这个地方错了！！！
        Page<BwsUser> allUser = userDao.findAllUserNoPassword(pageable);

        return ResponseResult.SUCCESS("查询成功").setData(allUser);
    }

    /**
     * 更新用户的密码
     *
     * @param verifyCode
     * @param bwsUser
     * @return
     */
    @Override
    public ResponseResult updatePassword(String verifyCode, BwsUser bwsUser) {
        //检查邮箱是否有填写
        String email = bwsUser.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱不可以为空.");
        }
        //根据邮箱去redis里拿验证
        //进行对比
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (redisVerifyCode == null || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误.");
        }
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        int result = userDao.updatePasswordByEmail(bCryptPasswordEncoder.encode(bwsUser.getPassword()), email);
        //修改密码
        return result > 0 ? ResponseResult.SUCCESS("密码修改成功") : ResponseResult.FAILED("密码修改失败");
    }

    /**
     * 更新用户的邮箱
     *
     * @param email
     * @param verifyCode
     * @return
     */
    @Override
    public ResponseResult updateEmail(String email, String verifyCode) {
//        确保已经登录
        BwsUser bwsUser = this.checkBwsUser();
        if (bwsUser == null) {
//            没有登录
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        对比验证码，确保新的邮箱是用户的
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(redisVerifyCode) || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误");
        }
//        验证码正确，删除占用的资源
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);

        int result = userDao.updateEmailById(email, bwsUser.getId());

        return result > 0 ? ResponseResult.SUCCESS("邮箱修改成功") : ResponseResult.FAILED("邮箱修改失败");
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public ResponseResult doLogout() {
//        拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        if (TextUtils.isEmpty(tokenKey)) {
//            用户没有登录
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        删除redis里面的token
        redisUtils.del(Constants.User.KEY_TOKEN+tokenKey);
//        删除MySQL里面的refreshToken
        refreshTokenDao.deleteAllByTokenKey(tokenKey);
//        删除cookies
        CookieUtils.deleteCookie(getResponse(),Constants.User.COOKIE_TOKEN_KEY);
        return ResponseResult.SUCCESS("退出登录成功");
    }


    /**
     * 解析 token 通过token_key
     *
     * @param tokenKey
     * @return BwsUser
     */
    private BwsUser parseByTokenKey(String tokenKey) {
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        if (token != null) {
            try {
                //解析token
                Claims claims = JwtUtils.parseJWT(token);
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
