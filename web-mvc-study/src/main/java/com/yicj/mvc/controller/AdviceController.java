package com.yicj.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/advice")
public class AdviceController {
    @GetMapping("/hello")
    public String hello(Date date, ModelMap modelMap){
        log.info("======> project name : {}", modelMap.get("project_name"));
        log.info("======> format date : {}",date == null ? null: new SimpleDateFormat("yyyy-MM-dd").format(date));
        throw new RuntimeException("异常了，跳转到ControllerAdvice的异常处理") ;
    }
}
