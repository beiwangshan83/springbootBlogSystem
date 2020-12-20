package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ICategoryService;
import com.beiwangshan.blog.service.IFriendLinkService;
import com.beiwangshan.blog.service.ILooperService;
import com.beiwangshan.blog.service.IWebSiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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



    @Autowired
    private IFriendLinkService friendLinkService;

    @Autowired
    private ILooperService looperService;

    @Autowired
    private IWebSiteInfoService webSiteInfoService;



    /**
     * 获取网站标题
     * @return
     */
    @GetMapping("/title")
    public ResponseResult getWebSiteTitle(){

        return webSiteInfoService.getWebSiteTitle();
    }

    /**
     * 获取访问量数据
     * @return
     */
    @GetMapping("/view_count")
    public ResponseResult getWebSiteViewCount(){

        return webSiteInfoService.getWebSiteViewCount();
    }

    /**
     * 获取seo信息
     *
     * @return
     */
    @GetMapping("/seo")
    public ResponseResult getWebSiteSeoInfo(){
        return webSiteInfoService.getSeoInfo();
    }


    /**
     * 获取轮播图列表 looper_list
     *
     * @return
     */
    @GetMapping("/loop")
    public ResponseResult getLoops(){
        return looperService.listLooper() ;
    }

//    获取友情链接列表 links

    /**
     * 获取轮播图列表 looper_list
     *
     * @return
     */
    @GetMapping("/friend_link")
    public ResponseResult getLinks(){
        return friendLinkService.listFriendLink();
    }

    /**
     * 统计网站的访问数量 PV -->  page view
     * 直接增加一个访问量的话，就可以刷访问数量
     * 正确的做法：
     *  根据ip --> 进行过滤，也可以集成第三方的一个统计数据api。
     *
     *  递增统计：
     *      通过redis进行统计，数据也会保存在MySQL里，但是不是每次都保存在MySQL里面，只有用户请求访问数量的时候才会保存更新一次。
     *      平时调用的时候，都是调用的redis里面的数据，增加redis里面的数据。
     *
     *  redis时机：
     *      每个页面进行访问的时候，如果在MySQL里面不存在，那么就去从MySQL里面读取数据，写到redis里面。
     *  如果有，则自增。
     *
     *  MySQL时机：
     *      当用户访问网站总访问量的时候，我们就从redis数据库里面进行读取，然后更新到MySQL的数据库中。
     *      如果redis里面没有，就从数据库里读取，放到redis里面。
     *
     *
     */
    @PutMapping("/view_count")
    public void updateViewCount(){
        webSiteInfoService.updateViewCount();
    }
}
