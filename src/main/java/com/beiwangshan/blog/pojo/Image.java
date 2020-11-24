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
@Table(name = "tb_images")
public class Image {

  @Id
  private String id;
  @Column(name= "`user_id`")
  private String userId;
  @Column(name= "`url`")
  private String url;
  @Column(name= "`name`")
  private String name;
  @Column(name= "`path`")
  private String path;
  @Column(name= "`content_type`")
  private String contentType;
  @Column(name= "`state`")
  private String state;
  @Column(name= "`create_time`")
  private Date createTime;
  @Column(name= "`update_time`")
  private Date updateTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
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
