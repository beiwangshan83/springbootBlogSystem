package com.beiwangshan.blog.service;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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


    /**
     * 获取图片，预览图片
     * @param response
     * @param imageId
     * @throws IOException
     */
    void viewImage(HttpServletResponse response, String imageId) throws IOException;

    /**
     * 获取图片列表
     * @param page
     * @param size
     * @return
     */
    ResponseResult listImages(int page, int size);
}
