package com.yicj.web.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableWebSecurity(debug = true)
@SpringBootApplication(exclude = ValidationAutoConfiguration.class)
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

    @ResponseBody
    @RequestMapping("/hello2")
    public String hello2(){
        return "Hello world" ;
    }

    @ResponseBody
    @RequestMapping("/hello3")
    public String hello3(){
        return "Hello world" ;
    }

    @ResponseBody
    @RequestMapping("/admin")
    public String admin(){
        return "Hello world" ;
    }
}
