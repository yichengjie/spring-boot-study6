package com.yicj.mvc.config;

import com.yicj.mvc.http.converter.properties.PropertiesHttpMessageConverter;
import com.yicj.mvc.web.method.support.PropertiesHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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

    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 插入到第一位，其他Converter向后移动
        converters.add(0,new PropertiesHttpMessageConverter());
        // 添加到最后时，结果会被MappingJackson2HttpMessageConverter处理返回json
        //converters.add(new PropertiesHttpMessageConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 自定义HandlerMethodArgumentResolver优先级低于内建HandlerMethodArgumentResolver，
        // 所有这里设置到第一位也没有用
        //resolvers.add(0, new PropertiesHandlerMethodArgumentResolver());
    }
}
