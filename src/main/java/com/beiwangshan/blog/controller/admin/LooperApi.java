package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.pojo.Looper;
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
@RequestMapping("/admin/loop")
public class LooperApi {

    /**
     * 上传轮播图的api
     * @return
     */
    @PostMapping
    public ResponseResult addLoop(@RequestBody Looper looper){

        return null;
    }

    /**
     * 删除轮播图的api
     * @param loopId
     * @return
     */
    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLooper(@PathVariable("loopId")String loopId){

        return null;
    }

    /**
     * 更新轮播图的api
     * @param loopId
     * @return
     */
    @PutMapping("/{loopId}")
    public ResponseResult updateLooper(@PathVariable("loopId")String loopId){

        return null;
    }


    /**
     * 获取轮播图的 api
     * @param loopId
     * @return
     */
    @GetMapping("/{looperId}")
    public ResponseResult getLooper(@PathVariable("loopId")String loopId){

        return null;
    }

    /**
     * 获取轮播图的列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listLooper(@RequestParam("apge")int page, @RequestParam("size")int size){

        return null;
    }
}
