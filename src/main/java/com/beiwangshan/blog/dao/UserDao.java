package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.BwsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @className: com.beiwangshan.blog.dao-> UserDao
 * @description: 用户的Dao，数据库相关的
 * @author: 曾豪
 * @createDate: 2020-11-18 1:08
 * @version: 1.0
 * @todo:
 */
public interface UserDao extends JpaRepository<BwsUser,String>, JpaSpecificationExecutor<BwsUser> {

    /**
     * 根据用户名来查找
     * @param userName
     * @return
     */
    BwsUser findOneByUserName(String userName);

    /**
     * 根据邮箱来查询
     * @param email
     * @return
     */
    BwsUser findOneByEmail(String email);

    /**
     * 根据邮箱或者用户名查询用户
     * @param email
     * @param userName
     * @return
     */
    List<BwsUser> findOneByEmailOrUserName(String email, String userName);

}
