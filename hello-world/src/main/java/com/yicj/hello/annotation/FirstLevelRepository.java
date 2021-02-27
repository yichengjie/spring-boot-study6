package com.yicj.hello.annotation;

import org.springframework.stereotype.Repository;
import java.lang.annotation.*;

@Repository
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstLevelRepository {
    String value() default "";
}
