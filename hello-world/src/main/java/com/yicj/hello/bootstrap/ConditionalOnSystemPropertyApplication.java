package com.yicj.hello.bootstrap;

import com.yicj.hello.condition.ConditionalOnSystemProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


@Slf4j
public class ConditionalOnSystemPropertyApplication {
    @Bean
    @ConditionalOnSystemProperty(name = "user.name", value = "yichengjie")
    public String helloWorld(){
        return "Hello, yicj" ;
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ConditionalOnSystemPropertyApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        String bean = ctx.getBean("helloWorld", String.class);
        log.info("====> bean : {}", bean);
        ctx.close();
    }
}
