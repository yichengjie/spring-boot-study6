package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.MyData;
import com.yicj.study.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @ResponseBody
    @GetMapping("/converter")
    public User getUserByConverter(User user){
        return user ;
    }


    @ResponseBody
    @PostMapping("/user")
    public User getUser(User user){
        return user ;
    }

    private MyData getMyData(){
        return MyData.builder().firstName("yi").lastName("cj").build() ;
    }
}
