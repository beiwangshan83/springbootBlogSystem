package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ArticleDao;
import com.beiwangshan.blog.pojo.Article;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @className: com.beiwangshan.blog.service.Impl-> TestSolrService
 * @description: solr的测试
 * @author: 曾豪
 * @createDate: 2020-12-07 19:34
 * @version: 1.0
 * @todo:
 */
@Service
public class TestSolrService {
    @Autowired
    private SolrClient client;

    public void add() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "111222333444");
        doc.addField("blog_view_count", 222);
        doc.addField("blog_title", "标题内容");
        doc.addField("blog_content", "搜索内容");
        doc.addField("blog_create_time", new Date());
        doc.addField("blog_labels", "标签");
        doc.addField("blog_url", "文章url");
        doc.addField("blog_category_id", "111222");
        try {
            client.add(doc);
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "111222333444");
        doc.addField("blog_view_count", 20);
        doc.addField("blog_title", "标题内容");
        doc.addField("blog_content", "搜索内容");
        doc.addField("blog_create_time", "创建时间");
        doc.addField("blog_labels", "标签");
        doc.addField("blog_url", "文章url");
        try {
            client.add(doc);
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
//            单独删除一条记录
            client.deleteById("111222333444");
//            删除所有记录
//            client.deleteByQuery("*");
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
//            单独删除一条记录
//            client.deleteById("111222333444");
//            删除所有记录
            client.deleteByQuery("*");
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ArticleDao articleDao;

    public void importAll() {
        List<Article> all = articleDao.findAll();
        for (Article article : all
        ) {

        }
    }
}
