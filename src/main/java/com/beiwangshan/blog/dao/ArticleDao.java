package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> ArticleDao
 * @description: 文章的Dao层
 * @author: 曾豪
 * @createDate: 2020-11-24 20:07
 * @version: 1.0
 * @todo:
 */
public interface ArticleDao extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {
    /**
     * 通过ID 查询一个 Article 对象
     * @return
     * @param articleId
     */
    Article findOneById(String articleId);
}
