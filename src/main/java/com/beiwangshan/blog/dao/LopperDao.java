package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> LopperDao
 * @description: 轮播图的Dao
 * @author: 曾豪
 * @createDate: 2020-11-24 10:27
 * @version: 1.0
 * @todo:
 */
public interface LopperDao extends JpaRepository<Looper,String>, JpaSpecificationExecutor<Looper> {

    /**
     * 根据ID 查询一个 loop对象
     * @param loopId
     * @return
     */
    Looper findOneById(String loopId);

    /**
     * 删除 轮播图
     * @param loopId
     * @return
     */
    int deleteOneById(String loopId);
}
