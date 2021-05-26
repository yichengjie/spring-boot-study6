package com.yicj.study;

import org.junit.Test;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class HelloWorldTest {

    @Test
    public void test1(){
        BinaryOperator <Long> add = (x, y) -> x + y ;
        BiFunction<Long, Long, Long> fun1 = add.andThen(r -> r + 1);
        Long apply = fun1.apply(1L, 2L);
        System.out.println(apply);
    }

    @Test
    public void test2(){
        String name = getUserName() ;
        //name = formatUserName(name) ;
        Button button = new Button("test") ;
        button.addActionListener(event -> System.out.println("hi : " + name));
    }

    private String formatUserName(String name) {

        return name ;
    }

    private String getUserName() {

        return "xxx" ;
    }


}
