package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> CategoryDao
 * @description: Category的Dao层
 * @author: 曾豪
 * @createDate: 2020-11-23 14:39
 * @version: 1.0
 * @todo:
 */
public interface CategoryDao extends JpaRepository<Category,String>, JpaSpecificationExecutor<Category> {
}
