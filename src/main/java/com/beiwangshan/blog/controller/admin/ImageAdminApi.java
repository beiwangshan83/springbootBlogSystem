package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @className: com.beiwangshan.blog.controller.admin-> ImageApi
 * @description: 图片相关的API
 * @author: 曾豪
 * @createDate: 2020-11-17 22:36
 * @version: 1.0
 * @todo:
 */
@RestController
@RequestMapping("/admin/image")
public class ImageAdminApi {

    @Autowired
    private IImageService imageService;

    /**
     * 上传图片的api
     *
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file){

        return imageService.uploadImage(file);
    }

    /**
     * 删除图片的api
     * @param imageId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId")String imageId){

        return null;
    }


    /**
     * 获取图片的 api
     * @param imageId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId")String imageId){
        try {
            imageService.viewImage(response,imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片的列表
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listImage(@RequestParam("apge")int page,@RequestParam("size")int size){

        return null;
    }
}
