package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @className: com.beiwangshan.blog.dao-> ImageDao
 * @description: 图片香瓜
 * @author: 曾豪
 * @createDate: 2020-11-23 20:29
 * @version: 1.0
 * @todo:
 */
public interface ImageDao extends JpaRepository<Image,String>, JpaSpecificationExecutor<Image> {

    /**
     * 删除图片
     *    根据修改图片的状态来实现
     * @param imageId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_images` SET `state` = '0' WHERE id =  ?")
    int deleteByUpdateState(String imageId);
}
