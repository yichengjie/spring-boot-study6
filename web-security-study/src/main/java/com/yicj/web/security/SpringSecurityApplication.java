package com.yicj.web.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableWebSecurity
@SpringBootApplication
public class SpringSecurityApplication {

    //https://www.cnblogs.com/yy3b2007com/p/12194142.html
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args) ;
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello world" ;
    }
}
