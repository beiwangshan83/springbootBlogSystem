package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.admin-> ImageApi
 * @description: 评论相关的API
 * @author: 曾豪
 * @createDate: 2020-11-17 22:36
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentAdminApi {

    /**
     * 删除评论的api
     * @param commentId
     * @return
     */
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){

        return null;
    }


    /**
     * 获取评论的列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listComment(@RequestParam("apge")int page, @RequestParam("size")int size){

        return null;
    }


    /**
     * 置顶评论
     * @param commentId
     * @return
     */
    @PutMapping("/top/{commentId}")
    public ResponseResult TopComment(@PathVariable("commentId")String commentId){

        return null;
    }
}
