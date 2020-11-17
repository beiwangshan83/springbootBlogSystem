package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.response.ResponseResult;
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
public class ArticleApi {

    /**
     * 上传文章的api
     * @return
     */
    @PostMapping
    public ResponseResult uploadArticle(@RequestBody Article article){

        return null;
    }

    /**
     * 删除文章的api
     * @param articleId
     * @return
     */
    @DeleteMapping("/{articleId}")
    public ResponseResult uploadArticle(@PathVariable("articleId")String articleId){

        return null;
    }

    /**
     * 更新文章的api
     * @param articleId
     * @return
     */
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId")String articleId){

        return null;
    }


    /**
     * 获取文章的 api
     * @param articleId
     * @return
     */
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
    @GetMapping("/list")
    public ResponseResult listArticle(@RequestParam("apge")int page, @RequestParam("size")int size){

        return null;
    }
}
