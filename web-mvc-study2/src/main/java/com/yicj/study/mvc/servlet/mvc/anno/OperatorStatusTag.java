package com.yicj.study.mvc.servlet.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperatorStatusTag {
    String success () default "操作成功";
    String error() default "操作失败";
}
