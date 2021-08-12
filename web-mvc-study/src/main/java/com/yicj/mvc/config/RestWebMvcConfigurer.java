package com.yicj.mvc.config;

import com.yicj.mvc.model.User;
import com.yicj.mvc.web.method.support.PropertiesHandlerMethodArgumentResolver;
import com.yicj.mvc.web.method.support.PropertiesHandlerMethodReturnValueHandler;
import com.yicj.mvc.web.servlet.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

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
        List<HandlerMethodReturnValueHandler> handlers =
                requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(handlers.size() + 1) ;
        newHandlers.add(new PropertiesHandlerMethodReturnValueHandler());
        newHandlers.addAll(handlers) ;
        // 重置Handler对象集合
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
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
       //converters.add(0,new PropertiesHttpMessageConverter());
        // 添加到最后时，结果会被MappingJackson2HttpMessageConverter处理返回json
        //converters.add(new PropertiesHttpMessageConverter());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new CustomInterceptor()) ;
    }

    /**
     * 配置内容协商
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true) ;
        configurer.favorPathExtension(true) ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*") ;
    }


    @Bean
    public ViewResolver myViewResolver(){
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver() ;
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(Ordered.LOWEST_PRECEDENCE -10);
        viewResolver.setContentType("text/xml;charset=UTF-8");
        return viewResolver ;
    }



    @Bean
    @Profile("dev")
    public User user1(){
        User user =  new User() ;
        user.setUsername("张三");
        return user ;
    }

    @Bean
    @Profile("test")
    public User user2(){
        User user =  new User() ;
        user.setUsername("李四");
        return user ;
    }


}
