package com.yicj.func;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


@Slf4j
public class Hello2Test {

    static void error_test(int n){
        if (n == 5) throw new RuntimeException("我就是喜欢来搞惊喜!") ;
        log.info("我消费的元素是---> " + n);
    }

    static void observable_error_test(int n){
        Observable.create(observer ->{
            try {
                observer.onNext(n);
                observer.onComplete();
            }catch (Exception e){
                observer.onError(e);
            }
        }).subscribe(x -> error_test((int)x), Throwable::printStackTrace, () ->{
            log.info("Emission completed");
        });
    }


    @Test
    public void observable_error_test_acc(){
        observable_error_test(1);
        observable_error_test(5);
    }


    static Integer error_test_p(int n){
        if (n == 5) throw new RuntimeException("我就是喜欢来搞惊喜!") ;
        log.info("我消费的元素是: " +n);
        return n + 10 ;
    }

    static Observable<Integer> error_test_pro(int n){
        return Observable.fromCallable(() -> error_test_p(n)) ;
    }

    @Test
    public void fromCallable_test(){
        error_test_pro(1).subscribe(x -> log(x),
                Throwable::printStackTrace,
                ()-> log("Emission completed")) ;
        log("***************我是一个大大的分割线*******************");
        error_test_pro(5).subscribe(x -> log(x),
                Throwable::printStackTrace,
                ()-> log("Emission completed")) ;
    }


    private void log(Object msg){
        System.out.println(Thread.currentThread().getName() + " : " + msg);
    }

    private void sleep(int timer){
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void timer_test(){
        Observable.timer(2, TimeUnit.SECONDS).subscribe(x -> log(10)) ;
        sleep(10000);
    }

}
