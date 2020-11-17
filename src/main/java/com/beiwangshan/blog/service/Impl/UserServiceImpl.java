package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.SettingsDao;
import com.beiwangshan.blog.dao.UserDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Setting;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Contants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

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

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

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
        bwsUser.setCreate_time(new Date());
        bwsUser.setUpdate_time(new Date());

//        保存到数据库
        userDao.save(bwsUser);

//        更新已经添加的标记
//        肯定没有的
        Setting setting = new Setting();
        setting.setId(String.valueOf(snowflakeIdWorker.nextId()));
        setting.setKey(Contants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        setting.setCreate_time(new Date());
        setting.setUpdate_time(new Date());
        setting.setValue("1");//1表示存在，0表示删除
        settingsDao.save(setting);


        return ResponseResult.SUCCESS("初始化成功");
    }
}
