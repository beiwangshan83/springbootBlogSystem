package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @className: com.beiwangshan.blog.dao-> LabelDao
 * @description: 博客标签的dao
 * @author: 曾豪
 * @createDate: 2020-11-18 14:06
 * @version: 1.0
 * @todo:
 */
public interface LabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {

    /**
     * 根据Id删除一个label
     * @param Id
     * @return
     */
    @Modifying
    int deleteOneById(String Id);

    /**
     * 根据Iid查找一个label
     * @param Id
     * @return
     */
    Label findOneById(String Id);

    /**
     * 根据名字进行查询
     * @param label
     */
    Label findOneByName(String label);

    @Modifying
    @Query(nativeQuery = true,value = "update `tb_labels` set `count` = `count` + 1 where `name` = ?")
    int updateCountByName(String labelName);


}
