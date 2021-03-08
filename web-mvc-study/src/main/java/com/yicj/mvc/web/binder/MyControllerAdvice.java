package com.yicj.mvc.web.binder;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
/*
@ControllerAdvice(
    //指定拦截的包
    basePackages = {"com.yicj.mvc.controller.*"},
    // 限定被标注@Controller的类才被拦截
    annotations = Controller.class
)*/
public class MyControllerAdvice {
    // 绑定格式化、参数转换规则和增加验证器等
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // 自定义日期编辑器，限定格式为 yyyy-MM-dd，且参数不容许为空
        CustomDateEditor dateEditor =
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),false) ;
        // 注册自定义日期编辑器
        binder.registerCustomEditor(Date.class, dateEditor);
    }
    // 在执行控制器前执行，可以初始化数据模型
    @ModelAttribute
    public void projectMode(Model model){
        model.addAttribute("project_name","web-mvc") ;
    }
    // 异常处理,当controller发生异常时，都能用相同的视图响应
    @ExceptionHandler(Exception.class)
    public String exception(Model model, Exception ex){
        log.info("==> 统一异常处理 : ", ex);
        model.addAttribute("exception_message", ex.getMessage()) ;
        //返回异常视图
        return "exception" ;
    }
}
