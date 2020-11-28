package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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


    /**
     * 删除文章 通过文章的ID
     * @param articleId
     * @return
     */
    @Modifying
    int deleteAllById(String articleId);

    /**
     * 通过更新文章的状态来删除文章的效果
     * @param articleId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_article` set `state` = '0' where id = ?")
    int deleteArticleByState(String articleId);


    /**
     * 根据文章的ID 查询 标签
     * @param articleId
     * @return
     */
    @Query(nativeQuery = true,value = "select labels from `tb_article` where `id` = ?")
    String listArticleLabelById(String articleId);


}
