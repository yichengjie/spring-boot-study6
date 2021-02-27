package com.yicj.hello.service.impl;

import com.yicj.hello.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Slf4j
@Service
@Profile("Java8")
public class Java8CalculateService implements CalculateService {
    @Override
    public Integer sum(Integer... values) {
        log.info("===> Java8 Lambda 表达式实现");
        Integer result = Stream.of(values).reduce(0 , Integer::sum) ;
        return result;
    }
}
