package com.yicj.mvc.web.method.support;

import com.yicj.mvc.http.converter.properties.PropertiesHttpMessageConverter;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * {@link Properties} {@link HandlerMethodReturnValueHandler}
 */
public class PropertiesHandlerMethodReturnValueHandler implements  HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 判断方法的返回值，是否为Properties
        Class<?> returnType1 = returnType.getMethod().getReturnType();
        return Properties.class.isAssignableFrom(returnType1);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 告知 spring mvc当前请求已经处理完毕，参考RequestResponseBodyMethodProcessor.handleReturnValue
        mavContainer.setRequestHandled(true);
        Properties properties = (Properties) returnValue ;
        PropertiesHttpMessageConverter converter = new PropertiesHttpMessageConverter() ;
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest ;
        HttpServletRequest request = servletWebRequest.getRequest();
        String contentType = request.getHeader("Content-Type");
        MediaType mediaType = MediaType.parseMediaType(contentType);
        //获取Servlet Response对象
        HttpServletResponse response = servletWebRequest.getResponse() ;
        HttpOutputMessage message = new ServletServerHttpResponse(response) ;
        converter.write(properties, mediaType, message);
    }
}
