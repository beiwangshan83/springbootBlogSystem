package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IArticleService;
import com.beiwangshan.blog.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: com.beiwangshan.blog.controller.portal.article-> ArticlePortalApi
 * @description: 前端页面文章的api
 * @author: 曾豪
 * @createDate: 2020-11-17 23:46
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/portal/article")
public class ArticlePortalApi {

    @Autowired
    private IArticleService articleService;

    /**
     * 获取文章列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticle(@PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.listArticle(page, size, null, null, Constants.Article.STATE_PUBLISH);
    }


    /**
     * 根据分类获取 文章列表
     *
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{categoryId}/{page}/{size}")
    public ResponseResult listArticleBycategoryId(@PathVariable("categoryId") String categoryId,
                                                  @PathVariable("page") int page,
                                                  @PathVariable("size") int size) {
        return articleService.listArticle(page, size, null, categoryId, Constants.Article.STATE_PUBLISH);
    }


    /**
     * 获取文章详情
     * * 权限，任意用户
     * * 内容过滤：
     * * 只允许拿置顶的或者已经发布成功的
     * * 其他的获取需要权限
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ResponseResult getArticleDetail(@PathVariable("categoryId") String categoryId) {
        return articleService.getArticleById(categoryId);

    }


    /**
     * 获取置顶文章 列表
     *
     * @return
     */
    @GetMapping("/top")
    public ResponseResult getTopArticles() {

        return articleService.getTopArticles();
    }

    /**
     * 获取推荐文章 列表
     * TODO：推荐算法
     * 简单做法：
     * 通过标签来计算匹配度，文章一定有标签，一个或者多个（5个以内）
     * 从里面随机拿一个标签 ---> 每次获取的推荐文章不那么雷同。
     * 通过标签去查询类似的文章，所包含此标签的文章
     * 如果没有相关文章，就获取最新的文章
     *
     * @return
     */
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticles(@PathVariable("articleId") String articleId,@PathVariable("size")int size) {

        return articleService.listRecommendArticle(articleId,size);
    }


    /**
     * 根据标签名 -- 获取文章列表
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/label/{label}/{page}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label") int label,@PathVariable("page")String page,@PathVariable("size")String size) {
        return null;
    }


    /**
     * 获取标签云
     * 用户点击标签，就会通过标签名，获取相关的文章列表
     *
     * @return
     */
    @GetMapping("/label/{size}")
    public ResponseResult getLabels(@PathVariable("size")String size) {

        return articleService.getTopArticles();
    }


}
