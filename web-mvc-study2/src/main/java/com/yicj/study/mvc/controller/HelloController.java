package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.MyData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/hello2")
    @ModelAttribute("data")
    public MyData hello2(){
        return getMyData() ;
    }

    private MyData getMyData(){
        return MyData.builder().firstName("yi").lastName("cj").build() ;
    }
}
