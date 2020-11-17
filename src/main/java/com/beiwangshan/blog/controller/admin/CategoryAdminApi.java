package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.Category;
import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.admin-> CategoryAdminApi
 * @description: 文章分类接口
 * @author: 曾豪
 * @createDate: 2020-11-17 22:17
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {

    /**
     * 添加分类的接口
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){

        return null;
    }

    /**
     * 删除文章的分类
     * @param categoryId
     * @return
     */
    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId")String categoryId){

        return null;
    }


    /**
     * 更新文章的分类，按照文章分类的ID
     * @param categoryId
     * @param category
     * @return
     */
    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId")String categoryId,@RequestBody Category category){

        return null;
    }

    /**
     *  获取分类信息
     * @param categoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId")String categoryId){

        return null;
    }


    /**
     * 获取分类的列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listCategories(@RequestParam("page")int page,@RequestParam("size")int size){

        return null;
    }

}
