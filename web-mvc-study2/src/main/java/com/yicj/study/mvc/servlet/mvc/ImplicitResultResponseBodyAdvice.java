package com.yicj.study.mvc.servlet.mvc;

import com.yicj.study.mvc.servlet.mvc.anno.ResultEnhancerTag;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ImplicitResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().isAnnotationPresent(ResultEnhancerTag.class) ;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Map<String,Object> res = new HashMap<>();
        String flag = String.valueOf(body);
        Method method = returnType.getMethod();
        ResultEnhancerTag annotation = method.getAnnotation(ResultEnhancerTag.class);
        if ("true".equalsIgnoreCase(flag)){
            res.put("code",200);
            res.put("msg",annotation.success()) ;
        }else if ("false".equalsIgnoreCase(flag)){
            res.put("code",500);
            res.put("msg",annotation.error()) ;
        }else {
            res.put("code",200);
            res.put("msg",annotation.success());
            res.put("data",body);
        }
        return res ;
    }
}
