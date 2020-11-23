package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.FriendLink;
import com.beiwangshan.blog.response.ResponseResult;
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

    /**
     * 上传友情链接的api
     * @return
     */
    @PostMapping
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){

        return null;
    }

    /**
     * 删除友情链接的api
     * @param friendLinkId
     * @return
     */
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return null;
    }

    /**
     * 更新友情链接的api
     * @param friendLinkId
     * @return
     */
    @PutMapping("/{friendLinkId}")
    public ResponseResult updateFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return null;
    }


    /**
     * 获取友情链接的 api
     * @param friendLinkId
     * @return
     */
    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId")String friendLinkId){

        return null;
    }

    /**
     * 获取友情链接的列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listFriendLink(@RequestParam("apge")int page,@RequestParam("size")int size){

        return null;
    }
}
