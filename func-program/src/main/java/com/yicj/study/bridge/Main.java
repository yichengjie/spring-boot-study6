package com.yicj.study.bridge;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.stream.Stream;

@Slf4j
public class Main {

    @Test
    public void test1(){
        MyPair datePair = new DateInterval();
        datePair.hello(new Object());
    }

    @Test
    public void test2(){
        Method[] declaredMethods = DateInterval.class.getDeclaredMethods();
        Stream.of(declaredMethods).forEach(method -> {
            log.info("name : {}, return type: {}", method.getName(), method.getReturnType());
        });
    }
}
