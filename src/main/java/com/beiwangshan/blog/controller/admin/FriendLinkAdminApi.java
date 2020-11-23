package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.FriendLinkService;
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
    private FriendLinkService friendLinkService;

    /**
     * 上传友情链接的api
     * @return
     */
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

        return null;
    }

    /**
     * 更新友情链接的api
     * @param friendLinkId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendLinkId}")
    public ResponseResult updateFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return null;
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
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listFriendLink(@PathVariable("page")int page,@PathVariable("size")int size){

        return friendLinkService.listFriendLink(page,size);
    }
}
