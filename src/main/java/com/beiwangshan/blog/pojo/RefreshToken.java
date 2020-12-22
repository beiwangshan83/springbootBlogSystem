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
@Table(name = "tb_refresh_token")
public class RefreshToken {

    @Id
    private String id;
    @Column(name = "`refresh_token`")
    private String refreshToken;
    @Column(name = "`user_id`")
    private String userId;
    @Column(name = "`token_key`")
    private String tokenKey;
    @Column(name = "`mobile_token_key`")
    private String mobileTokenKey;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileTokenKey() {
        return mobileTokenKey;
    }

    public void setMobileTokenKey(String mobileTokenKey) {
        this.mobileTokenKey = mobileTokenKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
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
