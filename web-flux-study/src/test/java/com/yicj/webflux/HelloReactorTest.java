package com.yicj.webflux;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.io.IOException;

public class HelloReactorTest {
    @Test
    public void test1() throws IOException {
        // onComplete在数据完整发送完毕后才会调用，如果只传部分数据将不会被回调
        Flux.just("A","B","C") // 发布A -> B -> C
            .map(value -> "+" + value)
            .subscribe(
                System.out::println, // 数据消费 = onNext(T)
                System.out::println, // 异常处理 = onError(Throwable)
                () -> System.out.println("完成操作"), // 完成回调 = onComplete()
                subscription ->{ // 背压操作 = onSubscribe(Subscription)
                    // 这里需要主动请求数据，否则无法接收到数据
                    subscription.request(1);
                    subscription.cancel();
                }
            ) ;
    }
    @Test
    public void test2() throws IOException {
        // onComplete在数据完整发送完毕后才会调用，如果只传部分数据将不会被回调
        Flux.just("A","B","C") // 发布A -> B -> C
            .map(value -> "+" + value)
            .subscribe(new Subscriber<String>() {
                private Subscription subscription ;
                @Override
                public void onSubscribe(Subscription s) {
                    this.subscription = s;
                    // 这里需要主动请求数据，否则无法接收到数据
                    this.subscription.request(1);
                }
                @Override
                public void onNext(String s) {
                    System.out.println(s);
                    // 这里需要主动请求数据，否则无法接收到数据
                    this.subscription.request(1);
                }
                @Override
                public void onError(Throwable t) {
                    System.err.println(t);
                }
                @Override
                public void onComplete() {
                    System.out.println("完成操作");
                }
            }) ;
    }

}
