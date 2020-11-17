package com.beiwangshan.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String helloWorld(){
        return "hello world";
    }

    @ResponseBody
    @GetMapping("test-json")
    public void testJson(){

    }
}
