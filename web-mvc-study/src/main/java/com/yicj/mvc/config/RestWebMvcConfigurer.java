package com.yicj.mvc.config;

import com.yicj.mvc.http.converter.properties.PropertiesHttpMessageConverter;
import com.yicj.mvc.web.method.support.PropertiesHandlerMethodArgumentResolver;
import com.yicj.mvc.web.method.support.PropertiesHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * REST {@link WebMvcConfigurer} 实现
 */
@Configuration
public class RestWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter ;

    @PostConstruct
    public void init(){
        // 获取当前RequestMappingHandlerAdapter中所有的Resolver对象
        //HandlerMethodArgumentResolverComposite.getResolvers()获取集合不可变，所以这里需要新建List
        List<HandlerMethodArgumentResolver> resolvers =
                requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> newResolvers = new ArrayList<>(resolvers.size() + 1) ;
        newResolvers.add(new PropertiesHandlerMethodArgumentResolver());
        newResolvers.addAll(resolvers) ;
        // 重置Resolver对象
        requestMappingHandlerAdapter.setArgumentResolvers(newResolvers);
        //----------------------------------------------------------------//
        // 注册PropertiesHandlerMethodReturnValueHandler
        /*List<HandlerMethodReturnValueHandler> handlers =
                requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(handlers.size() + 1) ;
        newHandlers.add(new PropertiesHandlerMethodReturnValueHandler());
        newHandlers.addAll(handlers) ;
        // 重置Handler对象集合
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);*/
    }


    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new PropertiesHandlerMethodReturnValueHandler());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 自定义HandlerMethodArgumentResolver优先级低于内建HandlerMethodArgumentResolver，
        // 详情见:
        // RequestMappingHandlerAdapter.afterPropertiesSet() => {
        //    List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
        //    new HandlerMethodArgumentResolverComposite().addResolvers(resolvers)
        // }
        // 所以这里添加了也没用，参数将被MapMethodProcessor处理，因为Properties extends Hashtable
        // 这里调整到@PostConstruct注解的init()中处理
        //resolvers.add(new PropertiesHandlerMethodArgumentResolver());
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 插入到第一位，其他Converter向后移动
        converters.add(0,new PropertiesHttpMessageConverter());
        // 添加到最后时，结果会被MappingJackson2HttpMessageConverter处理返回json
        //converters.add(new PropertiesHttpMessageConverter());
    }


}
