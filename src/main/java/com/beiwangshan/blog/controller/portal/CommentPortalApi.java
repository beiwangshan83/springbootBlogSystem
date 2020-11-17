package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.portal-> CommentPortalApi
 * @description: 前端评论相关Api
 * @author: 曾豪
 * @createDate: 2020-11-17 23:17
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {

    /**
     * 发表评论的api
     * @return
     */
    @PostMapping
    public ResponseResult PostComment(@RequestBody Comment comment){

        return null;
    }


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
     * 获取评论列表的api
     * @param articleId
     * @return
     */
    @GetMapping("/list/{articleId}")
    public ResponseResult listComment(@PathVariable("articleId")String articleId){

        return null;
    }
}
