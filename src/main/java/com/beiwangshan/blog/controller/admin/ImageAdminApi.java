package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 上传图片的api
     * @return
     */
    @PostMapping
    public ResponseResult uploadImage(){

        return null;
    }

    /**
     * 删除图片的api
     * @param imageId
     * @return
     */
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId")String imageId){

        return null;
    }

    /**
     * 更新图片的api
     * @param imageId
     * @return
     */
    @PutMapping("/{imageId}")
    public ResponseResult updateImage(@PathVariable("imageId")String imageId){

        return null;
    }


    /**
     * 获取图片的 api
     * @param imageId
     * @return
     */
    @GetMapping("/{imageId}")
    public ResponseResult getImage(@PathVariable("imageId")String imageId){

        return null;
    }

    /**
     * 获取图片的列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listImage(@RequestParam("apge")int page,@RequestParam("size")int size){

        return null;
    }
}
