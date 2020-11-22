package com.beiwangshan.blog.controller;

import com.beiwangshan.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: com.beiwangshan.blog.controller-> ErrorPageController
 * @description: 错误页面的控制类，错误码转统一返回的代码
 * @author: 曾豪
 * @createDate: 2020-11-22 19:28
 * @version: 1.0
 * @todo:
 */
@RestController
public class ErrorPageController {

    /**
     * 403 页面，定向
     * @return
     */
    @GetMapping("/403")
    public ResponseResult page403(){

        return ResponseResult.ERROR_403();
    }

    /**
     * 404 页面，定向
     * @return
     */
    @GetMapping("/404")
    public ResponseResult page404(){

        return ResponseResult.ERROR_404();
    }

    /**
     * 405 页面，定向
     * @return
     */
    @GetMapping("/405")
    public ResponseResult page405(){

        return ResponseResult.ERROR_405();
    }

    /**
     * 504 页面，定向
     * @return
     */
    @GetMapping("/504")
    public ResponseResult page504(){

        return ResponseResult.ERROR_504();
    }

    /**
     * 505 页面，定向
     * @return
     */
    @GetMapping("/505")
    public ResponseResult page505(){

        return ResponseResult.ERROR_505();
    }

}
