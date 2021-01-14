package com.beiwangshan.blog.controller.portal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: com.beiwangshan.blog.controller.portal-> AppApi
 * @description: app的下载相关
 * @author: 曾豪
 * @createDate: 2020-12-22 23:51
 * @version: 1.0
 * @todo:
 * 应用下载
 * 应用检查更新
 */
@RestController
@RequestMapping("/portal/app")
public class AppApi {


    /**
     * 第三方扫描登录二维码就下载App的接口
     * @param code
     * @return
     */
    @GetMapping("/{code}")
    public void downloadAppForPartScan(@PathVariable("code")String code,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        //TODO:直接把最新的app写出去
    }
}
