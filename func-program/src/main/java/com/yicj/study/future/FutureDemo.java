package com.yicj.study.future;

import com.yicj.study.Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureDemo {

    private String name = Constant.version ;

    public static void main(String[] args)  throws Exception{
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<String> future = pool.submit(()->{
            Thread.sleep(3000);
            return "异步任务计算结果!";
        }) ;
        // 提交完异步任务，主线程可以继续干一些其他的事
        doSomethingElse() ;
        //为了获取异步计算结果,我们可以通过future.get 和 轮询机制来获取
        String result ;
        // Get方式会导致当前线程阻塞，这显然违背了异步计算的初衷
        // result = future.get() ;
        // 轮询方式虽然不会导致当前线程阻塞，但会导致搞额cpu负载
        long start = System.currentTimeMillis() ;
        while (true) {
            if (future.isDone()) {
                break;
            }
        }
        log.info("轮询耗时：{}",System.currentTimeMillis() - start);
        result = future.get() ;
        log.info("获取到异步计算结果啦: {}", result);
        pool.shutdown();
    }

    private static void doSomethingElse() {
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        log.info("我的重要的事情干完了，我要获取异步计算结果来执行剩下的事情.");
    }

    public int inc(){
        int x ;
        try {
            x = 2 ;
            return x;
        }catch (Exception e){
            x = 5 ;
            return x ;
        }finally {
            x = 8 ;
        }
    }

}
