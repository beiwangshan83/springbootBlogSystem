package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> ImageDao
 * @description: 图片香瓜
 * @author: 曾豪
 * @createDate: 2020-11-23 20:29
 * @version: 1.0
 * @todo:
 */
public interface ImageDao extends JpaRepository<Image,String>, JpaSpecificationExecutor<Image> {

}
