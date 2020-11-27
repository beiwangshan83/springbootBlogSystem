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

    /**
     * 获取分类信息
     * @param categoryId
     * @return
     */
    ResponseResult getCategory(String categoryId);

    /**
     * 删除分类信息
     * @param categoryId
     * @return
     */
    ResponseResult deleteCategory(String categoryId);

    /**
     * 获取分类的列表
     * @param page
     * @param size
     * @return
     */
    ResponseResult listCategories();

    /**
     * 更新分类信息
     * @param categoryId
     * @return
     */
    ResponseResult updateCategory(String categoryId,Category category);
}
