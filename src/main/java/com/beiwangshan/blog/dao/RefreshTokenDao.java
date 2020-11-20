package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> RefreshTokenDao
 * @description: RefreshToken的DAO 层
 * @author: 曾豪
 * @createDate: 2020-11-20 15:46
 * @version: 1.0
 * @todo:
 */
public interface RefreshTokenDao extends JpaRepository<RefreshToken,String>, JpaSpecificationExecutor<RefreshToken> {

    /**
     * 通过 tokenKey 从数据库查询 一个 RefreshToken
     * @param tokenKey
     * @return
     */
    RefreshToken findOneByTokenKey(String tokenKey);
}
