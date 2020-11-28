package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> ICommentService
 * @description: 评论相关service
 * @author: 曾豪
 * @createDate: 2020-11-28 19:14
 * @version: 1.0
 * @todo:
 */
public interface ICommentService {

    /**
     * 上传评论
     * @param comment
     * @return
     */
    ResponseResult postComment(Comment comment);

    /**
     * 通过文章ID 来获取评论列表
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    ResponseResult listCommentByArticleId(String articleId, int page, int size);
}
