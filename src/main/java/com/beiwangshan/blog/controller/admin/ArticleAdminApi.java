package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @className: com.beiwangshan.blog.controller.admin-> ImageApi
 * @description: 文章相关的API
 * @author: 曾豪
 * @createDate: 2020-11-17 22:36
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {

    @Autowired
    private IArticleService articleService;

    /**
     * 上传文章的api
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addArticle(@RequestBody Article article){

        return articleService.addArticle(article);
    }

    /**
     * 删除文章的api
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId")String articleId){

        return null;
    }

    /**
     * 更新文章的api
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId")String articleId){

        return null;
    }


    /**
     * 获取文章的 api
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId")String articleId){

        return null;
    }

    /**
     * 获取文章的列表
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticle(@PathVariable("page")int page,
                                      @PathVariable("size")int size,
                                      @RequestParam(value = "state",required = false)String state,
                                      @RequestParam(value = "keyword",required = false)String keyword,
                                      @RequestParam(value = "categoryId",required = false)String categoryId){

        return articleService.listArticle(page,size,keyword,categoryId,state);
    }

    /**
     * 更新文章的状态
     * @param articleId
     * @param state
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/state/{articleId}/{state}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId,@PathVariable("state")String state){
        return null;
    }

    /**
     * 更新文章的置顶状态
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId){
        return null;
    }
}
