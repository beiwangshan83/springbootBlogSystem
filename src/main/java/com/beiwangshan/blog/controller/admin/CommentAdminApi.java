package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private ICommentService commentService;

    /**
     * 删除评论的api
     * @param commentId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){

        return commentService.deleteCommentById(commentId);
    }


    /**
     * 获取评论的列表
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listComment(@RequestParam("apge")int page, @RequestParam("size")int size){

        return commentService.listComments(page,size);
    }


    /**
     * 置顶评论
     * @param commentId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{commentId}")
    public ResponseResult TopComment(@PathVariable("commentId")String commentId){

        return commentService.TopComment(commentId);
    }
}
