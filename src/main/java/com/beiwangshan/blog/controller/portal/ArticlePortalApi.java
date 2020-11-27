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
        return articleService.listArticle(page,size,null,null, Constants.Article.STATE_PUBLISH);
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
        return articleService.listArticle(page,size,null,categoryId, Constants.Article.STATE_PUBLISH);
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
     * 获取推荐文章
     *
     * @return
     */
    @GetMapping("/top")
    public ResponseResult getRecommendArticleDetail() {

        return null;
    }

}
