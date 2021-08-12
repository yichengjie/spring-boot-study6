package com.yicj.study.web.controller;

import com.yicj.enchancer.annotation.EnableMvcCommonHandler;
import com.yicj.enchancer.servlet.mvc.annotation.ResultEnhancerTag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMvcCommonHandler
public class HelloController {

    @GetMapping("/hello")
    @ResultEnhancerTag(success = "删除用户成功", error = "删除用户失败")
    public boolean hello(){
        return true ;
    }

    @GetMapping(value = "/hello2", produces = {"application/json"})
    @ResultEnhancerTag(success = "删除用户成功", error = "删除用户失败")
    public String hello2(){
        return "my data" ;
    }
}
