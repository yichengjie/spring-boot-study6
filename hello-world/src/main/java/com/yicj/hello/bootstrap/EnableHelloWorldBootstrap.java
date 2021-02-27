package com.yicj.hello.bootstrap;

import com.yicj.hello.annotation.EnableHelloWorld;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableHelloWorld
public class EnableHelloWorldBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EnableHelloWorldBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
        String bean = ctx.getBean("helloWorld", String.class);
        log.info("=====> bean : {}", bean);
        ctx.close();
    }
}
