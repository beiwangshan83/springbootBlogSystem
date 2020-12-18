package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> ISolrService
 * @description: solr的service
 * @author: 曾豪
 * @createDate: 2020-12-14 22:47
 * @version: 1.0
 * @todo:
 */
public interface ISolrService {
    /**
     * 执行搜索
     * @param keyword
     * @param page
     * @param size
     * @param categoryId
     * @param sort
     * @return
     */
    ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort);

    /**
     * 添加文章到solr
     * @param article
     */
    void addArticle(Article article);

    /**
     * 从solr删除文章
     * @param articleId
     * @return
     */
    void delArticle(String articleId);

    /**
     * 更新solr里面的文章
     */
    void updateArticle(String articleId,Article article);
}
