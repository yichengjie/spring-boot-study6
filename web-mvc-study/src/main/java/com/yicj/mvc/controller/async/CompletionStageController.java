package com.yicj.mvc.controller.async;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * 异步 {@link RestController}
 */
@RestController
public class CompletionStageController {
    private final Random random = new Random(59) ;

    @GetMapping("/completionStageHello")
    public CompletionStage<String> completionStage(){
        return CompletableFuture.supplyAsync(()->{
            int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 模拟等待时间，rpc或db查询
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            return "CompletionStage Hello world ["+time+"] " + format;
        }) ;
    }
}
