package com.yicj.study.mvc.servlet.mvc.component;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.servlet.mvc.annotation.ResultEnhancerTag;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.lang.reflect.Method;


@ControllerAdvice
public class ImplicitResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().isAnnotationPresent(ResultEnhancerTag.class) ;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        JsonResult res = new JsonResult();
        String flag = String.valueOf(body);
        Method method = returnType.getMethod();
        // String 类型的结果不做处理
        if (method.getReturnType().isAssignableFrom(String.class)){
            return body ;
        }
        ResultEnhancerTag annotation = method.getAnnotation(ResultEnhancerTag.class);
        if ("true".equalsIgnoreCase(flag)){
            res.setCode(200) ;
            res.setMsg(annotation.success());
        }else if ("false".equalsIgnoreCase(flag)){
            res.setCode(500);
            res.setMsg(annotation.error()) ;
        }else {
            res.setCode(200);
            res.setMsg(annotation.success());
            res.setObj(body);
        }
        return res ;
    }
}
