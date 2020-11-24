package com.beiwangshan.blog.service;

import com.beiwangshan.blog.pojo.Looper;
import com.beiwangshan.blog.response.ResponseResult;

/**
 * @className: com.beiwangshan.blog.service-> LooperService
 * @description: 轮播图的service
 * @author: 曾豪
 * @createDate: 2020-11-24 10:20
 * @version: 1.0
 * @todo:
 */
public interface ILooperService {
    /**
     * 添加轮播图
     * @param looper
     * @return
     */
    ResponseResult addLoop(Looper looper);
}
