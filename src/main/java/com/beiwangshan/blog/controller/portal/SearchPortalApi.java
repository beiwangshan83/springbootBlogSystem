package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ISolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: com.beiwangshan.blog.controller.portal-> SearchPortalApi
 * @description: 搜索的api
 * @author: 曾豪
 * @createDate: 2020-11-18 0:08
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/portal/search")
public class SearchPortalApi {

    @Autowired
    private ISolrService solrService;

    /**
     * 查找
     *
     * @param keyword ==> 关键字
     * @param page ==> 分页
     * @return
     */
    @GetMapping
    public ResponseResult doSearch(@RequestParam("keyword")String keyword,
                                   @RequestParam("page")int page,
                                   @RequestParam("size")int size,
                                   @RequestParam(value = "categoryId",required = false)String categoryId,
                                   @RequestParam(value = "sort",required = false)Integer sort){
        return solrService.doSearch(keyword,page,size,categoryId,sort);
    }
}
