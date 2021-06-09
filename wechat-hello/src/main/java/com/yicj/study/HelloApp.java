package com.yicj.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author yicj1
 * @title: HelloApp
 * @description: TODO
 * @email yicj1@lenovo.com
 * @date 2021/6/8 16:06
 */
@SpringBootApplication
public class HelloApp {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringApplication.run(HelloApp.class, args) ;
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 将StringHttpMessageConverter编码设置为UTF-8，以解决后台收到中文乱码参数
        StringHttpMessageConverter stringHttpMessageConverter =
                new StringHttpMessageConverter(Charset.defaultCharset());
        restTemplate.getMessageConverters().set(1, stringHttpMessageConverter);
        return restTemplate ;
    }
}
