package com.beiwangshan.blog.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @className: com.beiwangshan.blog.pojo-> SearchResult
 * @description: 搜索结果的bean类
 * @author: 曾豪
 * @createDate: 2020-12-14 23:11
 * @version: 1.0
 * @todo:
 */
public class SearchResult implements Serializable {
    //blog_content,blog_create_time,blog_labels,blog_url,blog_title,blog_view_count
    private String blog_content;
    private Date blog_create_time;
    private String blog_labels;
    private String blog_url;
    private String blog_title;
    private String blog_view_count;

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public Date getBlog_create_time() {
        return blog_create_time;
    }

    public void setBlog_create_time(Date blog_create_time) {
        this.blog_create_time = blog_create_time;
    }

    public String getBlog_labels() {
        return blog_labels;
    }

    public void setBlog_labels(String blog_labels) {
        this.blog_labels = blog_labels;
    }

    public String getBlog_url() {
        return blog_url;
    }

    public void setBlog_url(String blog_url) {
        this.blog_url = blog_url;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_view_count() {
        return blog_view_count;
    }

    public void setBlog_view_count(String blog_view_count) {
        this.blog_view_count = blog_view_count;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "blog_content='" + blog_content + '\'' +
                ", blog_create_time=" + blog_create_time +
                ", blog_labels='" + blog_labels + '\'' +
                ", blog_url='" + blog_url + '\'' +
                ", blog_title='" + blog_title + '\'' +
                ", blog_view_count='" + blog_view_count + '\'' +
                '}';
    }
}
