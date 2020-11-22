package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.BwsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
     * @return BwsUser
     */
    BwsUser findOneByUserName(String userName);

    /**
     * 根据邮箱来查询
     * @param email
     * @return BwsUser
     */
    BwsUser findOneByEmail(String email);

    /**
     * 根据邮箱或者用户名查询用户
     * @param email
     * @param userName
     * @return List
     */
    List<BwsUser> findOneByEmailOrUserName(String email, String userName);

    /**
     * 通过userID查询一个用户对象
     *
     * @param userId
     * @return BwsUser
     */
    BwsUser findOneById(String userId);


    /**
     * 删除用户，通过修改用户的状态来实现
     * @param userId
     * @return int
     */
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE `tb_user` SET `state` = '0' WHERE `id` = ?")
    int deleteUserByState(String userId);


//    @Query(nativeQuery = true,value = "SELECT `id`, `user_name`,`roles`, `avatar`,`email`,`sign`,`state`, `reg_ip`,`login_ip`,`create_time`,`update_time` FROM `tb_user`")
//    @Query(nativeQuery = true,value = "select new BwsUser(b.id,b.userName,b.roles,b.avatar,b.email,b.sign,b.state,b.regIp,b.loginIp,b.createTime,b.updateTime) from BwsUser as b")
//    List<BwsUser> findAllUserNoPassword();

//    @Modifying
//    @Query(value = "select new com.beiwangshan.blog.pojo.BwsUser(u.id,u.userName,u.roles,u.avatar,u.email,u.sign,u.state,u.regIp,u.loginIp,u.createTime,u.updateTime) from BwsUser as u")
//    Page<BwsUser> listAllUserNoPassword(Pageable pageable);

}
