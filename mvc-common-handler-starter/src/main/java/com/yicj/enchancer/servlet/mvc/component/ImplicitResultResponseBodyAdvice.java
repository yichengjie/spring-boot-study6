package com.yicj.enchancer.servlet.mvc.component;

import com.yicj.enchancer.properties.MvcCommonHandlerProperties;
import com.yicj.enchancer.servlet.mvc.annotation.ResultEnhancerTag;
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

    private final MvcCommonHandlerProperties mvcCommonHandlerProperties ;

    public ImplicitResultResponseBodyAdvice(MvcCommonHandlerProperties mvcCommonHandlerProperties) {
        this.mvcCommonHandlerProperties = mvcCommonHandlerProperties;
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().isAnnotationPresent(ResultEnhancerTag.class) ;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Map<String, Object> res = new HashMap<>();
        String flag = String.valueOf(body);
        Method method = returnType.getMethod();
        // String 类型的结果不做处理
        if (method.getReturnType().isAssignableFrom(String.class)){
            return body ;
        }
        String statusFieldName = mvcCommonHandlerProperties.getResult().getStatusFieldName() ;
        String tipFieldName = mvcCommonHandlerProperties.getResult().getTipFieldName() ;
        String dataFieldName = mvcCommonHandlerProperties.getResult().getDataFieldName() ;
        ResultEnhancerTag annotation = method.getAnnotation(ResultEnhancerTag.class);
        if ("true".equalsIgnoreCase(flag)){
            res.put(statusFieldName,200) ;
            res.put(tipFieldName,annotation.success());
        }else if ("false".equalsIgnoreCase(flag)){
            res.put(statusFieldName,500);
            res.put(tipFieldName,annotation.error()) ;
        }else {
            res.put(statusFieldName, 200);
            res.put(tipFieldName, annotation.success());
            res.put(dataFieldName, body);
        }
        return res ;
    }
}