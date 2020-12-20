package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.interceptor.CheckTooFrequentCommit;
import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ICommentService commentService;

    /**
     * 发表评论的api
     * @return
     */
    @CheckTooFrequentCommit
    @PostMapping
    public ResponseResult PostComment(@RequestBody Comment comment){

        return commentService.postComment(comment);
    }


    /**
     * 删除评论的api
     * @param commentId
     * @return
     */
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){

        return commentService.deleteCommentById(commentId);
    }

    /**
     * 获取评论列表的api
     * @param articleId
     * @return
     */
    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComment(@PathVariable("articleId")String articleId,
                                      @PathVariable("page")int page,
                                      @PathVariable("size")int size){

        return commentService.listCommentByArticleId(articleId,page,size);
    }
}
