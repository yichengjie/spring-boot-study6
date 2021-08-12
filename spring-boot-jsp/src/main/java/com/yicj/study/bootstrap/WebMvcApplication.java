package com.yicj.study.bootstrap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication(scanBasePackages = "com.yicj.study")
public class WebMvcApplication {

    public static void main(String[] args) {
        //ConfigurableApplicationContext ctx = new SpringApplicationBuilder(WebMvcApplication.class)
        //        .web(WebApplicationType.SERVLET)
        //        .run(args);
        //ctx.close();
        SpringApplication.run(WebMvcApplication.class, args) ;
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello" ;
    }
}
