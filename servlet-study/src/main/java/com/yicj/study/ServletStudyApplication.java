package com.yicj.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan("com.yicj.study.web.servlet")
@SpringBootApplication(exclude = HttpEncodingAutoConfiguration.class)
public class ServletStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServletStudyApplication.class, args) ;
    }
}
