package com.yicj.mvc.web.servlet;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

// 加入ioc后，访问资源并未执行
//@Component
public class CustomWebRequestInterceptor implements WebRequestInterceptor {

    public CustomWebRequestInterceptor(){
        System.out.println("===========================");
    }

    @Override
    public void preHandle(WebRequest request) throws Exception {
        System.out.println("AllInterceptor...............................");
        //这个是放到request范围内的，所以只能在当前请求中的request中获取到
        request.setAttribute("request", "request", WebRequest.SCOPE_REQUEST);
        //这个是放到session范围内的，如果环境允许的话它只能在局部的隔离的会话中访问，否则就是在普通的当前会话中可以访问
        request.setAttribute("session", "session", WebRequest.SCOPE_SESSION);
        //如果环境允许的话，它能在全局共享的会话中访问，否则就是在普通的当前会话中访问
        //request.setAttribute("globalSession", "globalSession", 2);
        //System.out.println("CustomWebRequestInterceptor===> preHandle(request)");
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        System.out.println("CustomWebRequestInterceptor===> postHandle(request, model)");
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        System.out.println("CustomWebRequestInterceptor===> afterCompletion(request, ex)");
    }
}
