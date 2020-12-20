package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.interceptor.CheckTooFrequentCommit;
import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IFriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.admin-> ImageApi
 * @description: 友情链接相关的API
 * @author: 曾豪
 * @createDate: 2020-11-17 22:36
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/admin/friend_link")
public class FriendLinkAdminApi {

    @Autowired
    private IFriendLinkService friendLinkService;

    /**
     * 上传友情链接的api
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){

        return friendLinkService.addFriendLink(friendLink);
    }

    /**
     * 删除友情链接的api
     * @param friendLinkId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return friendLinkService.deleteFriendLink(friendLinkId);
    }

    /**
     * 更新友情链接的api
     *      更新内容：
     *          1.logo
     *          2.网站名称
     *          3.链接地址url
     *          4.order
     * @param friendLinkId
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendLinkId}")
    public ResponseResult updateFriendLink(@PathVariable("friendLinkId")String friendLinkId,
                                           @RequestBody FriendLink friendLink){

        return friendLinkService.updateFriendLink(friendLinkId,friendLink);
    }


    /**
     * 获取友情链接的 api
     * @param friendLinkId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return friendLinkService.getFriendLink(friendLinkId);
    }

    /**
     * 获取友情链接的列表
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listFriendLink(){

        return friendLinkService.listFriendLink();
    }
}
