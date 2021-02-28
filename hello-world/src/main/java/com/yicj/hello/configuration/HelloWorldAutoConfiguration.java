package com.yicj.hello.configuration;

import com.yicj.hello.annotation.EnableHelloWorld;
import com.yicj.hello.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring模式注解装配
@EnableHelloWorld // Spring @Enable 模块装配
@ConditionalOnSystemProperty(name = "user.name", value = "yichengjie")//条件装配
public class HelloWorldAutoConfiguration {

}
