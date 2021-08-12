package com.yicj.study.mvc;

import com.yicj.study.mvc.controller.HelloController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;

@Slf4j
public class HelloTest {

    @Test
    public void test1() throws Exception{
        Method method = HelloController.class.getMethod("hello4");
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
        log.info("type : {} ",resolvableType.isAssignableFrom(Boolean.class));
    }
}
