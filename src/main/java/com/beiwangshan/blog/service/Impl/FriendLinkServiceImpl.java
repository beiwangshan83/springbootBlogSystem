package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.FriendLinkDao;
import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.FriendLinkService;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @className: com.beiwangshan.blog.service.Impl-> FriendLinkServiceImpl
 * @description: 友情链接的实现类
 * @author: 曾豪
 * @createDate: 2020-11-23 16:44
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkDao friendLinkDao;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    /**
     * 添加友情链接
     *
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult addFriendLink(FriendLink friendLink) {
//        获取数据 判断数据
        String url = friendLink.getUrl();
        if (TextUtils.isEmpty(url)) {
            return ResponseResult.FAILED("链接URL不能为空");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmpty(logo)) {
            return ResponseResult.FAILED("LOGO不能为空");
        }
        String name = friendLink.getName();
        if (TextUtils.isEmpty(name)) {
            return ResponseResult.FAILED("友情链接名不能为空");
        }
//        补全数据
        friendLink.setId(snowflakeIdWorker.nextId()+"");
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
//        保存数据
        friendLinkDao.save(friendLink);
//        返回结果
        return ResponseResult.SUCCESS("友情链接保存成功");
    }
}
