package com.beiwangshan.blog.service;

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
}
