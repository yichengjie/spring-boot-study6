package com.yicj.hello.bootstrap;


import com.yicj.hello.repository.MyFirstLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan("com.yicj.hello.repository")
public class RepositoryBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(RepositoryBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        MyFirstLevelRepository bean = ctx.getBean("myFirstLevelRepository",MyFirstLevelRepository.class);
        log.info("======> {}", bean);
        ctx.close();
    }
}
