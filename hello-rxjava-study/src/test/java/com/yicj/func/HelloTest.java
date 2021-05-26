package com.yicj.func;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;


@Slf4j
public class HelloTest {

    @Test
    public void test1(){
        Observable<Object> observable = Observable.create(observer -> {
            observer.onNext("处理的数字是: " + Math.random() * 100);
            observer.onComplete();
        });
        observable.subscribe(consumer -> {
            log.info("我处理的元素是: {}", consumer) ;
        }) ;
        observable.subscribe(consumer -> {
            log.info("我处理的元素是：{}", consumer) ;
        }) ;
        new Thread(()->log.info("hello world..")).start();
    }

    @Test
    public void defer(){
        String [] monthArray = {"Jan","Feb","Mar", "Apl", "May","Jun","July","Aug","Sept","Oct","Nov","Dec"} ;
        Observable.defer(()-> Observable.fromArray(monthArray))
                .subscribe(System.out::println,Throwable::printStackTrace, ()->{
                    System.out.println("I am Done !! Completed normally");
                }) ;

    }

    @Test
    public void range(){
        log("Range test before");
        Observable.range(5,3)
                .subscribe(HelloTest::log) ;
        log("Range test after");
    }

    @Test
    public void just(){
        log("Just test before");
        Observable.just("Jan","Feb","Mar", "Apl", "May","Jun")
                .subscribe(HelloTest::log) ;
        log("Just test after");
    }

    @Test
    public void cache(){
        Observable<Object> observable = Observable.create(observer -> {
            observer.onNext("处理的数字是1: " + Math.random() * 100);
            observer.onNext("处理的数字是2: " + Math.random() * 100);
            observer.onComplete();
        }).cache();

        observable.subscribe(consumer ->{
            log.info("我处理的元素是:{}", consumer);
        }) ;

        observable.subscribe(consumer ->{
            log.info("我处理的元素是:{}", consumer);
        }) ;
    }


    public void test2(){

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+"job1 start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if(true){
                throw new RuntimeException("test");
            }else{
                System.out.println(Thread.currentThread()+"job1 exit,time->"+System.currentTimeMillis());
                return 1.2;
            }
        });

        CompletableFuture<Double> cf2=cf.whenComplete((a,b)->{

        }) ;
    }


    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName() + " : " + msg);
    }
}
