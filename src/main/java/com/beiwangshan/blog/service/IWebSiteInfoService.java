package com.beiwangshan.blog.service;

import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> IWebSiteInfoService
 * @description: 网站信息
 * @author: 曾豪
 * @createDate: 2020-11-24 18:51
 * @version: 1.0
 * @todo:
 */
public interface IWebSiteInfoService {
    /**
     * 获取网站的标题
     * @return
     */
    ResponseResult getWebSiteTitle();

    /**
     * 更新网站的标题
     * @param title
     * @return
     */
    ResponseResult updateWebSiteTitle(String title);

    /**
     * 获取网站seo信息
     * @return
     */
    ResponseResult getSeoInfo();

    /**
     * 修改seo 信息
     * @param keywords
     * @param description
     * @return
     */
    ResponseResult putSeoInfo(String keywords, String description);

    /**
     * 获取统计信息 view_count
     * @return
     */
    ResponseResult getWebSiteViewCount();

    /**
     * 统计网站的访问数量 PV -->  page view
     */
    void updateViewCount();
}
