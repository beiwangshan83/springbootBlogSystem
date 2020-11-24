package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IWebSiteInfoService;
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
@RequestMapping("/admin/web_site_info")
public class WebSiteInfoAdminApi {

    @Autowired
    private IWebSiteInfoService webSiteInfoService;

    /**
     * 获取网站标题
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/title")
    public ResponseResult getWebSiteTitle(){

        return webSiteInfoService.getWebSiteTitle();
    }

    /**
     * 修改网站标题
     *
     * @param title
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/title")
    public ResponseResult updateWebSiteTitle(@RequestParam("title")String title){

        return webSiteInfoService.updateWebSiteTitle(title);
    }


    /**
     * 获取seo信息
     *
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/seo")
    public ResponseResult getSeoInfo(){
        return webSiteInfoService.getSeoInfo();
    }

    /**
     * 修改网站SEO信息
     *
     * @param keywords => 关键字
     * @param description => 网站描述
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords")String keywords,@RequestParam("description")String description){
        return webSiteInfoService.putSeoInfo(keywords,description);
    }


    /**
     * 获取统计信息 view_count
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/view_count")
    public ResponseResult getWebSiteViewCount(){

        return webSiteInfoService.getWebSiteViewCount();
    }
}
