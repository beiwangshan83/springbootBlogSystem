package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.Category;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    ICategoryService categoryService;

    /**
     * 添加分类的接口
     * 需要管理员权限
     * 需要添加的数据：
     *      1.分类名称
     *      2.分类的pinyin
     *      3.顺序
     *      4.描述
     *
     * @param category
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){

        return categoryService.addCategory(category);
    }


    /**
     * 删除文章的分类
     *      使用情况：
     *          1.
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId")String categoryId){

        return categoryService.deleteCategory(categoryId);
    }


    /**
     * 更新文章的分类，按照文章分类的ID
     * @param categoryId
     * @param category
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId")String categoryId,@RequestBody Category category){

        return null;
    }

    /**
     *  获取分类信息
     *      使用情况：
     *          1.修改的时候获取一下
     *          2.填充弹窗
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId")String categoryId){

        return categoryService.getCategory(categoryId);
    }


    /**
     * 获取分类的列表
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listCategories(@PathVariable("page")int page,@PathVariable("size")int size){

        return categoryService.listCategories(page,size);
    }

}
