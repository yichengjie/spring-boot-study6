package com.yicj.hello.bootstrap;

import com.yicj.hello.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.yicj.hello.service")
public class CalculateProfileApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CalculateProfileApplication.class)
                .web(WebApplicationType.NONE)
                .profiles("Java8")
                .run(args);
        CalculateService bean = ctx.getBean(CalculateService.class);
        log.info("=====> bean : {}", bean.sum(1,2,3,4,5,6,7,8,9,10));
        ctx.close();
    }
}
