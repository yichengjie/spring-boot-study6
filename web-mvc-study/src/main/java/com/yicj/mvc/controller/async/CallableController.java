package com.yicj.mvc.controller.async;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Hello 异步{@link RestController}
 */
@RestController
public class CallableController {
    private final Random random = new Random(59) ;
    @GetMapping("/callableHello")
    public Callable<String> callableHello(){
        // 随机超时时间
        return ()-> {
            int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
            Thread.sleep(time);
            // 模拟等待时间，rpc或db查询
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            return "Callable Hello world ["+time+"] " + format;
        };
    }
}
