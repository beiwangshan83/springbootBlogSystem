package com.beiwangshan.blog.service;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: com.beiwangshan.blog.service-> IImageService
 * @description: 图片相关的service
 * @author: 曾豪
 * @createDate: 2020-11-23 20:24
 * @version: 1.0
 * @todo:
 */
public interface IImageService {

    /**
     * 上传图片
     * @param file
     * @return
     */
    ResponseResult uploadImage(MultipartFile file);
}
