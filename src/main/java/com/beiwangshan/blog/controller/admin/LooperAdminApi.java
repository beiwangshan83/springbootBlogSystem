package com.beiwangshan.blog.controller.admin;

import com.beiwangshan.blog.interceptor.CheckTooFrequentCommit;
import com.beiwangshan.blog.pojo.Looper;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.ILooperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class LooperAdminApi {

    @Autowired
    private ILooperService looperService;

    /**
     * 上传轮播图的api
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLoop(@RequestBody Looper looper){

        return looperService.addLoop(looper);
    }

    /**
     * 删除轮播图的api
     * @param loopId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLooper(@PathVariable("loopId")String loopId){

        return looperService.deleteLooper(loopId);
    }

    /**
     * 更新轮播图的api
     * @param loopId
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{loopId}")
    public ResponseResult updateLooper(@PathVariable("loopId")String loopId,@RequestBody Looper looper){

        return looperService.updateLooper(loopId,looper);
    }


    /**
     * 获取轮播图的 api
     * @param loopId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{loopId}")
    public ResponseResult getLooper(@PathVariable("loopId")String loopId){

        return looperService.getLooper(loopId);
    }

    /**
     * 获取轮播图的列表
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLooper(){

        return looperService.listLooper();
    }
}
