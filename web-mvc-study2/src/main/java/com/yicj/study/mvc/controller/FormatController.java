package com.yicj.study.mvc.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FormatController {

    @ResponseBody
    @PostMapping("/format/commit")
    public Map<String, Object> format(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date date,
          @NumberFormat(pattern = "#,###.##") Double number){
        Map<String, Object> dataMap = new HashMap<>() ;
        dataMap.put("date", date) ;
        dataMap.put("number", number) ;
        return dataMap ;
    }


    @ResponseBody
    @PostMapping("/format/commit2")
    public Map<String, Object> format2(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date date){
        Map<String, Object> dataMap = new HashMap<>() ;
        dataMap.put("date", date) ;
        return dataMap ;
    }
}
