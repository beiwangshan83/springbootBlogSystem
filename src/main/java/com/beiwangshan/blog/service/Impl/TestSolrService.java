package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ArticleDao;
import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.utils.Constants;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", article.getId());
            doc.addField("blog_view_count", article.getViewCount());
            doc.addField("blog_title", article.getTitle());
            /**
             * 对内容进行处理：
             *  第一种是Markdown写的 type = 1
             *  第二种是HTML写的 type = 0
             *
             *  如果是 type = 1 转成 HTML
             *  再由 HTML 转换为 纯文本
             *
             *  如果 type = 0
             *  提取纯文本
             */
            String type = article.getType();
            String html;
            if (Constants.Article.TYPE_MARKDOWN.equals(type)) {
                // markdown to html
                MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                        TablesExtension.create(),
                        JekyllTagExtension.create(),
                        TocExtension.create(),
                        SimTocExtension.create()
                ));
                Parser parser = Parser.builder(options).build();
                HtmlRenderer renderer = HtmlRenderer.builder(options).build();

                Node document = parser.parse(article.getContent());
                html = renderer.render(document);
            } else {
                html = article.getContent();
            }
            //到这，不管是什么都会是HTML了
            //HTML转text
            String text = Jsoup.parse(html).text();

            doc.addField("blog_content", text);
            doc.addField("blog_create_time", article.getCreateTime());
            doc.addField("blog_labels", article.getLabel());
            doc.addField("blog_url", "url");
            doc.addField("blog_category_id", article.getCategoryId());
            try {
                client.add(doc);
                client.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
