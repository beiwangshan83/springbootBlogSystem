package com.beiwangshan.blog.controller.admin;

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
@RequestMapping("/admin/web_size_info")
public class WebSiteInfoApi {

    /**
     * 获取网站标题
     * @return
     */
    @GetMapping("/title")
    public ResponseResult getWebSiteTitle(){

        return null;
    }

    /**
     * 修改网站标题
     *
     * @param title
     * @return
     */
    @PutMapping("/title")
    public ResponseResult updateWebSiteTitle(@RequestParam("title")String title){

        return null;
    }


    /**
     * 获取seo信息
     *
     * @return
     */
    @GetMapping("/seo")
    public ResponseResult getSeoInfo(){
        return null;
    }

    /**
     * 修改网站SEO信息
     *
     * @param keywords => 关键字
     * @param description => 网站描述
     * @return
     */
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords")String keywords,@RequestParam("description")String description){
        return null;
    }


    /**
     * 获取统计信息 view_count
     * @return
     */
    @GetMapping("/view_count")
    public ResponseResult getWebSiteViewCount(){

        return null;
    }
}
