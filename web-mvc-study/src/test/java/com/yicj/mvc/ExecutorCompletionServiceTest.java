package com.yicj.mvc;

import org.junit.Test;

import java.util.concurrent.*;

public class ExecutorCompletionServiceTest {

    @Test
    public void test1(){
        ExecutorService pool = Executors.newFixedThreadPool(3) ;
        CompletionService completionService = new ExecutorCompletionService(pool) ;
        completionService.submit(()->{
            Thread.sleep(100);
            return 1 ;
        }) ;
        completionService.submit(()->{
            Thread.sleep(200);
            return 2 ;
        }) ;
        completionService.submit(()->{
            Thread.sleep(300);
            return 3 ;
        }) ;
        int count = 0 ;
        while (count < 3){
            if (completionService.poll() != null){
                count ++ ;
            }
        }
        pool.shutdown();
    }
}
