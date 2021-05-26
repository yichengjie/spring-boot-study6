package com.yicj.func;

import com.yicj.fork.Calculator;
import com.yicj.fork.ExecutorServiceCalculator;
import com.yicj.fork.ForLoopCalculator;
import com.yicj.fork.ForkJoinCalculator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

@Slf4j
public class ForkJoinPoolTest {
    @Test
    public void test1(){
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        Calculator calculator = new ForLoopCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result);
    }

    @Test
    public void test2(){
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        Calculator calculator = new ExecutorServiceCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result); // 打印结果500500
    }

    @Test
    public void test3(){
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        ForkJoinCalculator calculator = new ForkJoinCalculator() ;
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result); // 打印结果500500
    }


    @Test
    public void test4(){
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0, 10000000L).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result); // 打印结果500500
    }


}
