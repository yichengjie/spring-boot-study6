package com.yicj.study.shiro.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello world" ;
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin(){
        return "hello admin" ;
    }
}
