package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 通过文章Id 删除 评论
     * @param articleId
     * @return
     */
    int deleteAllByArticleId(String articleId);

    /**
     * 通过文章ID 查询评论
     * @param ArticleId
     * @param pageable
     * @return
     */
    Page<Comment> findAllByArticleId(String ArticleId, Pageable pageable);
}
