package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> CommentDao
 * @description: 评论的Dao层
 * @author: 曾豪
 * @createDate: 2020-11-20 9:35
 * @version: 1.0
 * @todo:
 */
public interface CommentDao extends JpaRepository<Comment, String>, JpaSpecificationExecutor<Comment> {
    /**
     * 根据ID 查询
     *
     * @param commentId
     * @return
     */
    Comment findOneById(String commentId);
}
