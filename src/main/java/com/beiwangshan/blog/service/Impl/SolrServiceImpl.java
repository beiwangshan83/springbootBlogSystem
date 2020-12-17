package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.pojo.PageList;
import com.beiwangshan.blog.pojo.SearchResult;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ISolrService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @className: com.beiwangshan.blog.service.Impl-> SolrServiceImpl
 * @description: ISolrService实现类
 *      时机：
 *      搜索内容添加：
 *      文章发表时候，也就是状态为 1
 *      搜索内容删除：
 *      文章删除的时候，包括物理删除和修改文章的状态删除
 *      搜索内容更新：
 *      TODO:当阅读量更新
 * @author: 曾豪
 * @createDate: 2020-12-14 22:47
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
public class SolrServiceImpl extends BaseService implements ISolrService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort) {
        //1.检查page 和 size
        page = checkPage(page);
        size = checkSize(size);
        //2.分页设置
        SolrQuery solrQuery = new SolrQuery();
        //先设置每页的数量
        solrQuery.setRows(size);
        //设置开始位置
        int start = (page - 1) * size;
        solrQuery.setStart(start);
        //3.设置搜索条件
        //关键字，条件过滤，排序，高亮
        solrQuery.set("df", "search_item");
        if (TextUtils.isEmpty(keyword)) {
            solrQuery.set("q", "*");
        } else {
            solrQuery.set("q", keyword);
        }
        //排序有四个：根据时间==》 升序（1） 降序（2）；根据浏览量==》 升序（3） 降序（4）
        if (sort != null) {
            if (sort == Constants.SearchSort.TIME_ORDER_ASC) {
                solrQuery.setSort("blog_create_time", SolrQuery.ORDER.asc);
            } else if (sort == Constants.SearchSort.TIME_ORDER_DESC) {
                solrQuery.setSort("blog_create_time", SolrQuery.ORDER.desc);
            } else if (sort == Constants.SearchSort.VIEW_COUNT_ASC) {
                solrQuery.setSort("blog_view_count", SolrQuery.ORDER.asc);
            } else if (sort == Constants.SearchSort.VIEW_COUNT_DESC) {
                solrQuery.setSort("blog_view_count", SolrQuery.ORDER.desc);
            }
        }
        //分类
        if (!TextUtils.isEmpty(categoryId)) {
            solrQuery.setFilterQueries("blog_category_id:"+categoryId);
        }

        //高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("blog_title,blog_content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        solrQuery.setHighlightFragsize(500);


        solrQuery.addField("id,blog_content,blog_create_time,blog_labels,blog_url,blog_title,blog_view_count");
        //4.搜索 处理搜索结果
        try {
            QueryResponse result = solrClient.query(solrQuery);
            //获取到高亮内容
            Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();
            List<SearchResult> resultList = result.getBeans(SearchResult.class);
            //包含内容
            //5.返回搜索结果
            for (SearchResult item : resultList){
                Map<String, List<String>> stringListMap = highlighting.get(item.getId());
                    List<String> blogContent = stringListMap.get("blog_content");
                if (blogContent != null) {
                    item.setBlogContent(blogContent.get(0));
                }
                    List<String> blogTitle = stringListMap.get("blog_title");
                if (blogTitle != null) {
                    item.setBlogTitle(blogTitle.get(0));
                }
            }
            //列表，每页数量，页面
            long numFound = result.getResults().getNumFound();
            PageList<SearchResult> pageList = new PageList<>(page,numFound,size);
            pageList.setContents(resultList);
            //返回结果
            return ResponseResult.SUCCESS("搜索成功").setData(pageList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseResult.FAILED("搜索失败，请稍后重试。");
    }
}
