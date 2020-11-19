package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.SettingsDao;
import com.beiwangshan.blog.dao.UserDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Setting;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Contants;
import com.beiwangshan.blog.utils.RedisUtil;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Random;

/**
 * @className: com.beiwangshan.blog.service.Impl-> UserServiceImpl
 * @description: 初始化管理员账号的实现类
 * @author: 曾豪
 * @createDate: 2020-11-18 0:30
 * @version: 1.0
 * @todo:
 *  Transactional 用来操作数据库
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

    @Override
    public ResponseResult initManagerAccount(BwsUser bwsUser, HttpServletRequest request) {
//        检查是否初始化
        Setting managerAccountState = settingsDao.findOneByKey(Contants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        if (managerAccountState!=null){
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
        bwsUser.setRoles(Contants.User.ROLE_ADMIN);
        bwsUser.setAvatar(Contants.User.DEFAULT_AVATAR);
        bwsUser.setState(Contants.User.DEFAULT_STATE);
//        获取IP
        String localAddr = request.getLocalAddr();//本地ip
        String remoteAddr =  request.getRemoteAddr();//代理ip
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
        setting.setKey(Contants.Settings.MANAGER_ACCOUNT_INIT_STATE);
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

    @Override
    public void createCaptcha(HttpServletResponse response, String captchaKey) throws Exception{
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

//        保存在redis里
        redisUtil.set(Contants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);

        // 输出图片流
        tagetCaptcha.out(response.getOutputStream());
    }
}
