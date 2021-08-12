package com.yicj.hello.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class HelloApplication {
    public static void main(String[] args) {
        run1(args) ;
        //run2(args) ;
    }

    private static void run1(String[] args){
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(HelloApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        ctx.close();
    }

    public static void run2(String[] args){
        SpringApplication.run(HelloApplication.class, args) ;
    }
}
