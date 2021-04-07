package com.yicj.study.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public Map<String, Object> hello(){
        Map<String, Object> map = new HashMap<>() ;
        map.put("name", "Yicj") ;
        return map ;
    }
}
