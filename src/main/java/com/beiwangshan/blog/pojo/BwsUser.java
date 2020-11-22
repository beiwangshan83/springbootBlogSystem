package com.beiwangshan.blog.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * @author 曾豪
 */
@Entity
@Table(name = "tb_user")
public class BwsUser {

    /**
     * 空的构造函数
     */
    public BwsUser() {
    }

    /**
     * 定义自己的构造方法 ，除了密码不需要，其他的都需要
     *
     * @param id
     * @param userName
     * @param roles
     * @param avatar
     * @param email
     * @param sign
     * @param state
     * @param regIp
     * @param loginIp
     * @param createTime
     * @param updateTime
     */
    public BwsUser(String id, String userName,
                   String roles, String avatar,
                   String email, String sign,
                   String state, String regIp,
                   String loginIp, Date createTime,
                   Date updateTime) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        this.avatar = avatar;
        this.email = email;
        this.sign = sign;
        this.state = state;
        this.regIp = regIp;
        this.loginIp = loginIp;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 用户id
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 用户角色
     */
    @Column(name = "roles")
    private String roles;
    /**
     * 用户头像
     */
    @Column(name = "avatar")
    private String avatar;
    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 用户签名
     */
    @Column(name = "sign")
    private String sign;
    /**
     * 用户状态 0表示删除，1表示正常
     */
    @Column(name = "state")
    private String state;
    /**
     * 注册ip地址
     */
    @Column(name = "reg_ip")
    private String regIp;
    /**
     * 登录ip地址
     */
    @Column(name = "login_ip")
    private String loginIp;
    /**
     * 用户创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 用户登录时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegIp() {
        return regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
