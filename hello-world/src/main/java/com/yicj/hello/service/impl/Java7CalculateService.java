package com.yicj.hello.service.impl;

import com.yicj.hello.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("Java7")
public class Java7CalculateService implements CalculateService {
    @Override
    public Integer sum(Integer... values) {
        log.info("===> Java7 for 循环实现");
        Integer result = 0 ;
        for (Integer value : values){
            result += value ;
        }
        return result;
    }
}
