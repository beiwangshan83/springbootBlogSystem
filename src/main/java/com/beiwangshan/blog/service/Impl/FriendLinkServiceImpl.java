package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.FriendLinkDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.IFriendLinkService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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
@Transactional(rollbackOn = Exception.class)
public class FriendLinkServiceImpl extends BaseService implements IFriendLinkService {

    @Autowired
    private FriendLinkDao friendLinkDao;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private IUserService userService;

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
        friendLink.setId(snowflakeIdWorker.nextId() + "");
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
//        保存数据
        friendLinkDao.save(friendLink);
//        返回结果
        return ResponseResult.SUCCESS("友情链接保存成功");
    }

    /**
     * 获取友情链接
     *
     * @param friendLinkId
     * @return
     */
    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        FriendLink oneById = friendLinkDao.findOneById(friendLinkId);
        if (oneById == null) {
            return ResponseResult.FAILED("友情链接不存在");
        }
        return ResponseResult.SUCCESS("友情链接查询成功").setData(oneById);
    }

    /**
     * 获取友情链接列表
     *
     * @return
     */
    @Override
    public ResponseResult listFriendLink() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime", "order");
        List<FriendLink> allFriendLink;

        BwsUser bwsUser = userService.checkBwsUser();
        String roles = bwsUser.getRoles();
        List<FriendLink> categories;
        if (bwsUser == null || !Constants.User.ROLE_ADMIN.equals(roles)) {
            // 用户未登录，或者是非管理员的正常用户，只能获取到正常状态的文章
            categories = friendLinkDao.listFriendLinkByState("1");

        }else{
            categories = friendLinkDao.findAll(sort);
        }

        return ResponseResult.SUCCESS("友情链接列表获取成功").setData(categories);
    }



    /**
     * 删除友链，彻底的删除
     *
     * @param friendLinkId
     * @return
     */
    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int result = friendLinkDao.deleteAllById(friendLinkId);
        if (result == 0) {
            return ResponseResult.FAILED("删除失败，该友情链接不存在");
        }
        return ResponseResult.SUCCESS("友情链接删除成功");
    }

    /**
     * 更新友链
     * 更新内容：
     * 1.logo
     * 2.网站名称
     * 3.链接地址url
     * 4.order
     *
     * @param friendLinkId
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
//        检查数据
        FriendLink friendLinkFromDb = friendLinkDao.findOneById(friendLinkId);
        if (friendLinkFromDb == null) {
            return ResponseResult.FAILED("更新失败，该友情链接不存在");
        }
        String logo = friendLink.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            friendLinkFromDb.setLogo(logo);
        }
        String name = friendLink.getName();
        if (!TextUtils.isEmpty(name)) {
            friendLinkFromDb.setName(name);
        }
        String url = friendLink.getUrl();
        if (!TextUtils.isEmpty(url)) {
            friendLinkFromDb.setUrl(url);
        }
        friendLinkFromDb.setOrder(friendLink.getOrder());
        friendLinkFromDb.setUpdateTime(new Date());
//        保存数据
        friendLinkDao.save(friendLinkFromDb);
        return ResponseResult.SUCCESS("友情链接更新成功").setData(friendLinkFromDb);
    }
}
