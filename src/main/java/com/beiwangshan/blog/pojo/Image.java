package com.beiwangshan.blog.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Date;


@Entity
@Table(name = "tb_images")
public class Image {

  @Id
  private String id;
  @Column(name= "`user_id`")
  private String user_id;
  @Column(name= "`url`")
  private String url;
  @Column(name= "`state`")
  private String state;
  @Column(name= "`create_time`")
  private Date create_time;
  @Column(name= "`update_time`")
  private Date update_time;


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

  public Date getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  public Date getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Date update_time) {
    this.update_time = update_time;
  }
}
