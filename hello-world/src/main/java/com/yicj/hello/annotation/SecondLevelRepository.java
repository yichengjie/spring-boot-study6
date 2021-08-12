package com.yicj.hello.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@FirstLevelRepository
public @interface SecondLevelRepository {
    String value() default "";
}
