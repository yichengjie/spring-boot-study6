package com.yicj.study.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class LoginController {
    // 这里必须用RequestMapping而不能使用GetMapping
    //1. 未登录直接访问其他页面，此时会以GET方式跳转到这里
    //2. 登录用户名密码错误，此时会以POST方式跳转到这里
    @RequestMapping("/doLogin")
    public ModelAndView login(ModelAndView model, @RequestAttribute(required = false) String shiroLoginFailure) {
        //FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME
        model.setViewName("login");
        if ("org.apache.shiro.authc.UnknownAccountException".equalsIgnoreCase(shiroLoginFailure)){
            model.addObject("shiroLoginFailure", "用户名不存在！") ;
        }else if ("org.apache.shiro.authc.IncorrectCredentialsException".equalsIgnoreCase(shiroLoginFailure)){
            model.addObject("shiroLoginFailure", "密码错误！") ;
        }else if (!StringUtils.isEmpty(shiroLoginFailure)){
            model.addObject("shiroLoginFailure","登录失败!") ;
        }
        log.info("model : {}", model);
        return model;
    }
}
