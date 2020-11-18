package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> LabelDao
 * @description: 博客标签的dao
 * @author: 曾豪
 * @createDate: 2020-11-18 14:06
 * @version: 1.0
 * @todo:
 */
public interface LabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {
}
