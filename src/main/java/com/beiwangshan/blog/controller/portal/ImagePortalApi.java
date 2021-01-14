package com.beiwangshan.blog.controller.portal;

import com.beiwangshan.blog.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @className: com.beiwangshan.blog.controller.portal-> ImagePortalApi
 * @description: 前端图片获取的api
 * @author: 曾豪
 * @createDate: 2020-12-06 23:01
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/portal/image")
public class ImagePortalApi {

    @Autowired
    private IImageService imageService;

    /**
     * 获取图片的 api
     *
     * @param imageId
     * @return
     */
    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId") String imageId) {
        try {
            imageService.viewImage(response, imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取登录的图片，显示出来
     *
     * @param code
     */
    @GetMapping("/qr-code/{code}")
    public void getQrCodeImage(@PathVariable("code") String code, HttpServletResponse response, HttpServletRequest request) {
        imageService.createQrCode(code,response,request);
    }
}
