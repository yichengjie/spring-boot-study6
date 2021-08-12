package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Format2Controller {

    @ResponseBody
    @PostMapping("/format2/commit")
    public Map<String,Object> format3(String date, Integer number){
        Map<String, Object> dataMap = new HashMap<>() ;
        dataMap.put("date", date) ;
        dataMap.put("number", number) ;
        return dataMap ;
    }

}
