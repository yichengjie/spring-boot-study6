package com.yicj.hello.condition;


import org.springframework.context.annotation.Conditional;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(OnSystemPropertyCondition.class)
public @interface ConditionalOnSystemProperty{
    // Java 系统属性名称
    String name() default "" ;
    // Java 系统属性值
    String value() default "" ;
}
