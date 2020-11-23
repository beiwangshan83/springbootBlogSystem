package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.Category;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> ICategoryService
 * @description: 文章管理service
 * @author: 曾豪
 * @createDate: 2020-11-23 14:23
 * @version: 1.0
 * @todo:
 */
public interface ICategoryService {

    /**
     * 添加文章分类
     * @param category
     * @return
     */
    ResponseResult addCategory(Category category);
}
