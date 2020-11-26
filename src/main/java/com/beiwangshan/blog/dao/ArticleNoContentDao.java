package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> ArticleDao
 * @description: 文章的Dao层 不查询文章内容
 * @author: 曾豪
 * @createDate: 2020-11-24 20:07
 * @version: 1.0
 * @todo:
 */
public interface ArticleNoContentDao extends JpaRepository<ArticleNoContent,String>, JpaSpecificationExecutor<ArticleNoContent> {
    /**
     * 通过ID 查询一个 Article 对象
     * @return
     * @param articleId
     */
    Article findOneById(String articleId);
}
