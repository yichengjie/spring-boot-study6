package com.yicj.study.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class HelloController {
    //定义耗时操作
    private String doThing(String msg) {
        try {
            log.info("do thing ......");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("error ", e);
        }
        return msg;
    }

    @GetMapping("/common")
    public String commonHandle(){
        log.info("common-start... ");
        // 执行耗时操作
        return doThing("common handler") ;
    }

    @GetMapping("/mono")
    public Mono<String> monoHandle(){
        log.info("mono-start...");
        // 执行耗时操作
        Mono<String> mono = Mono.fromSupplier(() -> doThing("mono handle"));
        // Mono 表示包含0或1个元素的异步序列
        return mono ;
    }
}
