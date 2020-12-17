package com.beiwangshan.blog.pojo;

import org.apache.solr.client.solrj.beans.Field;

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

    @Field("id")
    private String id;
    @Field("blog_content")
    private String blogContent;
    @Field("blog_create_time")
    private Date blogCreateTime;
    @Field("blog_labels")
    private String blogLabels;
    @Field("blog_url")
    private String blogUrl;
    @Field("blog_title")
    private String blogTitle;
    @Field("blog_view_count")
    private int blogViewCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public Date getBlogCreateTime() {
        return blogCreateTime;
    }

    public void setBlogCreateTime(Date blogCreateTime) {
        this.blogCreateTime = blogCreateTime;
    }

    public String getBlogLabels() {
        return blogLabels;
    }

    public void setBlogLabels(String blogLabels) {
        this.blogLabels = blogLabels;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public int getBlogViewCount() {
        return blogViewCount;
    }

    public void setBlogViewCount(int blogViewCount) {
        this.blogViewCount = blogViewCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "blogContent='" + blogContent + '\'' +
                ", blogCreateTime=" + blogCreateTime +
                ", blogLabels='" + blogLabels + '\'' +
                ", blogUrl='" + blogUrl + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogViewCount='" + blogViewCount + '\'' +
                '}';
    }
}
