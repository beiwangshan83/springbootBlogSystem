package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> IArticleService
 * @description: 文章的service接口层
 * @author: 曾豪
 * @createDate: 2020-11-24 19:55
 * @version: 1.0
 * @todo:
 */
public interface IArticleService {
    /**
     * 添加文章
     * @param article
     * @return
     */
    ResponseResult addArticle(Article article);

    /**
     * 获取文章列表
     * @param page
     * @param size
     * @param keyword
     * @param categoryId
     * @return
     */
    ResponseResult listArticle(int page, int size, String keyword, String categoryId,String state);

    /**
     * 获取文章的详情
     * @param articleId
     * @return
     */
    ResponseResult getArticleById(String articleId);

    /**
     * 更新文章
     * @param articleId
     * @param article
     * @return
     */
    ResponseResult updateArticle(String articleId, Article article);

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    ResponseResult deleteArticleById(String articleId);

    /**
     * 更新文章的状态，通过更新状态来删除文章
     * @param articleId
     * @return
     */
    ResponseResult deleteArticleByUpdateState(String articleId);

    /**
     * 更新文章的置顶状态
     * @param articleId
     * @return
     */
    ResponseResult topArticle(String articleId);

    /**
     * 获取指定文章列表
     * @return
     */
    ResponseResult getTopArticles();

    /**
     * 获取相关推荐的文章
     * @param articleId
     * @param size
     * @return
     */
    ResponseResult listRecommendArticle(String articleId, int size);

    /**
     * 通过标签查询文章
     *
     * @param page
     * @param size
     * @param label
     * @return
     */
    ResponseResult listArticleByLable(int page, int size, String label);

    /**
     * 获取标签云
     * @param size
     * @return
     */
    ResponseResult listLabels(int size);
}
