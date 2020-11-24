package com.beiwangshan.blog.service;

import com.beiwangshan.blog.utils.Constants;

/**
 * @className: com.beiwangshan.blog.service-> BaseService
 * @description: 基础service
 * @author: 曾豪
 * @createDate: 2020-11-24 9:14
 * @version: 1.0
 * @todo:
 */
public class BaseService {
    public int checkPage(int page){
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        return page;
    }

    public int checkSize(int size){
        if (size < Constants.Page.DEFAULT_SIZE) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        return size;
    }
}
