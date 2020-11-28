package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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


    /**
     * 根据label查询类似的文章，但是不查询本类型的
     * @param label
     * @param size
     * @param originalArticleId
     * @return
     */
    @Query(nativeQuery = true,value = "select * from `tb_article` where `labels` like ? and `id` != ? and(`state` = '1' or `state` = '3') limit ?")
    List<ArticleNoContent> listArticleByLikeLabel(String label,String originalArticleId,int size);


    /**
     * 通过差值查询最新的文章作为推荐文章的补充
     * 因为有时候通过label查询的类似文章不够
     * @param size
     * @return
     */
    @Query(nativeQuery = true,value = "select * from `tb_article` where `id` != ?  and(`state` = '1' or `state` = '3')  order by `create_time` DESC limit ?")
    List<ArticleNoContent> listLastArticleBySize(String originalArticleId,int size);
}
