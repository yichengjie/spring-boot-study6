package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.User;
import com.yicj.study.mvc.validation.UserValidator;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ValidatorController {

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new UserValidator());
        CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false);
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    @ResponseBody
    @GetMapping("/validator/stringToUserConverter")
    public Map<String,Object> stringToUserConverter(@Validated User user, Errors errors, Date date){
        Map<String,Object> map = new HashMap<>() ;
        map.put("user", user) ;
        map.put("date", date) ;
        // 判断是否存在错误
        if (errors.hasErrors()){
            // 获取全部错误
            List<ObjectError> oes = errors.getAllErrors();
            oes.stream().forEach(oe ->{
                if(oe instanceof FieldError){
                    // 字段错误
                    FieldError fe = (FieldError) oe ;
                    map.put(fe.getField(), fe.getDefaultMessage()) ;
                }else {// 对象错误
                    map.put(oe.getObjectName(), oe.getDefaultMessage()) ;
                }
            });
        }
        return map ;
    }
}
