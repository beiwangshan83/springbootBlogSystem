package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @className: com.beiwangshan.blog.dao-> CategoryDao
 * @description: Category的Dao层
 * @author: 曾豪
 * @createDate: 2020-11-23 14:39
 * @version: 1.0
 * @todo:
 */
public interface CategoryDao extends JpaRepository<Category,String>, JpaSpecificationExecutor<Category> {

    /**
     * 通过分类ID 查询分类信息
     * @param categoryId
     * @return
     */
    Category findOneById(String categoryId);


    /**
     * 通过ID 删除 分类
     * @return
     */
//    int deleteAllById();


    /**
     * 删除分类，这里只是更新分类的状态
     * @param categoryId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_categories` SET `status` = '0' WHERE id =  ?")
    int deleteCategoryByUpdateStatus(String categoryId);
}
