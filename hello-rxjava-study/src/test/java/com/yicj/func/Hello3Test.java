package com.yicj.func;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Hello3Test {

    @Test
    public void infinite_unsubscribed_cache_thread_test() throws Exception{
        Observable<Object> observable = Observable.create(observer -> {
            Runnable runnable = () -> {
                BigInteger i = BigInteger.ZERO;
                while (!observer.isDisposed()) {
                    observer.onNext(i);
                    i = i.add(BigInteger.ONE);
                    log.info("下一个消费的数字 : {}", i.toString());
                }
            };
            new Thread(runnable).start();
        }).cache();

        Disposable subscribe1 = observable.subscribe(x -> log.info("一郎神: {}", x));
        Disposable subscribe2 = observable.subscribe(x -> log.info("二郎神: {}", x));
        TimeUnit.MILLISECONDS.sleep(100);
        subscribe1.dispose();
        subscribe2.dispose();
        log.info("我取消订阅了");
        TimeUnit.MILLISECONDS.sleep(100);
        log.info("程序结束");
    }


    // 这里如果改为之前的cache,JVM很快就会发生OOM,但是保持现状，程序运行平稳,很流畅
    @Test
    public void infinite_publish_test(){
        ConnectableObservable<Object> observable = Observable.create(observer ->{
            BigInteger i = BigInteger.ZERO;
            while (true){
                observer.onNext(i);
                i = i.add(BigInteger.ONE) ;
            }
        }).publish();
        observable.subscribe(x -> log.info("x = {}", x)) ;
        observable.connect() ;
    }

    @Test
    public void hot_publish_observable(){
        ConnectableObservable<Object> observable = Observable.create(observer -> {
            log.info("Establishing connection");
            observer.onNext("处理的数字是 : " + Math.random() *100);
            observer.onNext("处理的数字是 : " + Math.random() *100);
            observer.onComplete();
        }).publish();
        //
        observable.subscribe(consumer -> log.info("一郎神 : {}", consumer)) ;
        observable.subscribe(consumer -> log.info("二郎神 : {}", consumer)) ;
        //
        observable.connect() ;
    }


}
