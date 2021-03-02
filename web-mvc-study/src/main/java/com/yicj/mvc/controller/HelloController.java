package com.yicj.mvc.controller;

import com.yicj.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@RestController
public class HelloController {

    @CrossOrigin
    @GetMapping("/hello")
    public Map<String,String> hello(String message){
        Map<String,String> map = new HashMap<>() ;
        map.put("code","200") ;
        map.put("msg","success") ;
        map.put("data", message) ;
        return map ;
    }

    @PostMapping(value = "/hello2",
            //consumes = "application/xxx;charset=GBK",
            produces = "application/json;charset=UTF-8")
    public User hello2(@RequestBody User user){

        return user ;
    }
}
