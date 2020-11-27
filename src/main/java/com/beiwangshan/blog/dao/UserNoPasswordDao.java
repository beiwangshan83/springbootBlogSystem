package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.BwsUserNoPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> UserDao
 * @description: 用户的Dao，数据库相关的
 * @author: 曾豪
 * @createDate: 2020-11-18 1:08
 * @version: 1.0
 * @todo:
 */
public interface UserNoPasswordDao extends JpaRepository<BwsUserNoPassword,String>, JpaSpecificationExecutor<BwsUserNoPassword> {

}
