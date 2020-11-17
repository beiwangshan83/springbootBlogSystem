package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: com.beiwangshan.blog.controller.portal-> WebSiteInfoApi
 * @description: 网站信息的API
 * @author: 曾豪
 * @createDate: 2020-11-17 23:59
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/portal/web_site_info")
public class WebSiteInfoPortalApi {

    /**
     * 获取文章分类
     *
     * @return
     */
    @GetMapping("/categories")
    public ResponseResult getCategories(){
        return null;
    }

    /**
     * 获取网站标题
     * @return
     */
    @GetMapping("/title")
    public ResponseResult getWebSiteTitle(){

        return null;
    }

    /**
     * 获取访问量数据
     * @return
     */
    @GetMapping("/view_count")
    public ResponseResult getWebSiteViewCount(){

        return null;
    }

    /**
     * 获取seo信息
     *
     * @return
     */
    @GetMapping("/seo")
    public ResponseResult getWebSiteSeoInfo(){
        return null;
    }


    /**
     * 获取轮播图列表 looper_list
     *
     * @return
     */
    @GetMapping("/loop")
    public ResponseResult getLoops(){
        return null;
    }

//    获取友情链接列表 links

    /**
     * 获取轮播图列表 looper_list
     *
     * @return
     */
    @GetMapping("/friend_link")
    public ResponseResult getLinks(){
        return null;
    }
}
