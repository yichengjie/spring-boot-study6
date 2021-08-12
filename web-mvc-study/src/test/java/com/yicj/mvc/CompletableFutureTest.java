package com.yicj.mvc;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    @Test
    public void test1() throws Exception{
        CompletableFuture
                .runAsync(() -> {
                    System.out.println("111");
                }).thenRun(() -> {
                    System.out.println("222");
                }).thenRun(()->{
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("333");
                }).whenComplete((result, throwable) -> {
                    System.out.println("加载完成 :" + result);
                })/*.join()*/;// 等待完成
        System.out.println("xxx");
    }
}
