package com.yicj.mvc.controller.async;

import com.yicj.mvc.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Hello 异步 {@link RestController}
 */
@Slf4j
@RestController
@EnableScheduling
public class DeferredResultController {
    //DeferredResult 原理解析
    //https://juejin.cn/post/6934963596765954084
    private final BlockingQueue<DeferredResult<String>> queue = new ArrayBlockingQueue<>(5) ;
    private final Random random = new Random(59) ;

    //@Scheduled(fixedRate = 500000)
    public void process() throws InterruptedException{
        log.info("=======================");
        DeferredResult<String> result = queue.take() ;
        // 随机超时时间
        int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
        Thread.sleep(time);
        // 模拟等待时间，rpc或db查询
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        result.setResult("Hello world " + format) ;
    }

    @GetMapping("/deferredHello")
    public DeferredResult<String> deferredHello(){
        DeferredResult<String> result = new DeferredResult<>(500L,"timeout") ;
        log.info("deferredHello start... ");
        queue.offer(result) ;
        // 完成回调函数
        result.onCompletion(()-> log.info("HelloAsyncController 执行结束.."));
        // 超时回调函数
        result.onTimeout(()-> log.info("HelloAsyncController 执行超时.."));
        return result ;
    }
}
