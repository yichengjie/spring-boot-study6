package com.yicj.enchancer.annotation;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

// 非springboot项目可以使用这个导入
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MvcCommonHandlerRegistrar.class)
public @interface EnableMvcCommonHandler {
    boolean value() default true ;
}
