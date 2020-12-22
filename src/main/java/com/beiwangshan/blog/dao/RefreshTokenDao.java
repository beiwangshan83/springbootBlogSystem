package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
     * 默认的是 PC 端的 token_key
     * @param tokenKey
     * @return
     */
    RefreshToken findOneByTokenKey(String tokenKey);


    /**
     * 通过 mobile_token去查询 refreshToken
     * @param mobileTokenKey
     * @return
     */
    RefreshToken findOneByMobileTokenKey(String mobileTokenKey);

    /**
     * 根据用户Id删除 RefreshToken
     * @param userId
     * @return
     */
    int deleteAllByUserId(String userId);

    /**
     * 通过userId查找refreshToken
     * @param userId
     * @return
     */
    RefreshToken findOneByUserId(String userId);

    /**
     * 根据 tokenKey 删除
     * @param tokenKey
     * @return
     */
    int deleteAllByTokenKey(String tokenKey);

    /**
     * 删除 移动端的 token_key
     * @param tokenKey
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_refresh_token` set `mobile_token_key` = '' where  `mobile_token_key` = ?")
    void deleteMobileTokenKey(String tokenKey);

    /**
     * 删除 PC 端的token_key
     * @param tokenKey
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_refresh_token` set `token_key` = '' where  `token_key` = ?")
    void deletePcTokenKey(String tokenKey);
}
