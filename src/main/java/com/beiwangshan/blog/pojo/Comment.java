package com.beiwangshan.blog.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Date;


@Entity
@Table(name = "tb_comment")
public class Comment {

  @Id
  private String id;
  @Column(name= "`parent_content`")
  private String parent_content;
  @Column(name= "`article_id`")
  private String article_id;
  @Column(name= "`content`")
  private String content;
  @Column(name= "`user_id`")
  private String user_id;
  @Column(name= "`user_avatar`")
  private String user_avatar;
  @Column(name= "`user_name`")
  private String user_name;
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

  public String getParent_content() {
    return parent_content;
  }

  public void setParent_content(String parent_content) {
    this.parent_content = parent_content;
  }

  public String getArticle_id() {
    return article_id;
  }

  public void setArticle_id(String article_id) {
    this.article_id = article_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUser_avatar() {
    return user_avatar;
  }

  public void setUser_avatar(String user_avatar) {
    this.user_avatar = user_avatar;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
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
