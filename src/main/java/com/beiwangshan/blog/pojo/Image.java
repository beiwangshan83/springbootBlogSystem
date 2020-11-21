package com.beiwangshan.blog.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Date;

/**
 * @author 曾豪
 */
@Entity
@Table(name = "tb_images")
public class Image {

  @Id
  private String id;
  @Column(name = "`user_id`")
  private String user_id;
  @Column(name = "`url`")
  private String url;
  @Column(name = "`state`")
  private String state;
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

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
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
