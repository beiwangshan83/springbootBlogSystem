package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @className: com.beiwangshan.blog.dao-> FriendLinkDao
 * @description: 友情链接
 * @author: 曾豪
 * @createDate: 2020-11-23 16:43
 * @version: 1.0
 * @todo:
 */
public interface FriendLinkDao extends JpaRepository<FriendLink,String>, JpaSpecificationExecutor<FriendLink> {
    /**
     * 查询单个友情链接，通过id
      * @param friendLinkId
     * @return
     */
    FriendLink findOneById(String friendLinkId);

    /**
     * 根据 Id 删除
     * @param friendLinkId
     * @return
     */
    int deleteAllById(String friendLinkId);


    /**
     * 通过状态查询列表
     * @param state
     * @return
     */
    @Modifying
    @Query(nativeQuery = true,value = "select * from `tb_friends` where `state` = ? ")
    List<FriendLink> listFriendLinkByState(String state);
}
