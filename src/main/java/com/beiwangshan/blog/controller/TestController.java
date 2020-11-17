package com.beiwangshan.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String helloWorld(){
        log.info("hello world...");
        return "hello world";
    }

    @ResponseBody
    @GetMapping("test-json")
    public void testJson(){

    }
}
