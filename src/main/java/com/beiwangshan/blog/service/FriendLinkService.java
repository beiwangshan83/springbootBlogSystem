package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> FriendLinkService
 * @description: 友情链接
 * @author: 曾豪
 * @createDate: 2020-11-23 16:43
 * @version: 1.0
 * @todo:
 */
public interface FriendLinkService {
    /**
     * 添加友情链接
     * @param friendLink
     * @return
     */
    ResponseResult addFriendLink(FriendLink friendLink);
}
