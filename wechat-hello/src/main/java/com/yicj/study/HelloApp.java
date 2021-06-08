package com.yicj.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author yicj1
 * @title: HelloApp
 * @description: TODO
 * @email yicj1@lenovo.com
 * @date 2021/6/8 16:06
 */
@SpringBootApplication
public class HelloApp {

    public static void main(String[] args) {

        SpringApplication.run(HelloApp.class, args) ;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate() ;
    }
}
