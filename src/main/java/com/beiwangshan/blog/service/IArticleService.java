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
}
