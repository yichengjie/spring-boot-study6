package com.yicj.mvc.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * {@link Properties} {@link HandlerMethodArgumentResolver}
 */
public class PropertiesHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Properties.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest ;
        HttpServletRequest request = servletWebRequest.getRequest();
        String contentType = request.getHeader("Content-Type") ;
        MediaType mediaType = MediaType.parseMediaType(contentType);
        Charset charset = mediaType.getCharset();
        charset = charset == null ? Charset.forName("UTF-8") : charset ;
        // 请求输入字节流
        ServletInputStream inputStream = request.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream,charset);
        Properties properties = new Properties() ;
        // 加载字符流成为Properties对象
        properties.load(reader);
        return properties;
    }
}
