package com.yicj.enchancer.configuration;

import com.yicj.enchancer.properties.MvcCommonHandlerProperties;
import com.yicj.enchancer.servlet.mvc.component.ImplicitResultResponseBodyAdvice;
import com.yicj.enchancer.servlet.mvc.component.UnifiedExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MvcCommonHandlerProperties.class)
public class MvcCommonHandlerConfiguration {

    @Bean
    public ImplicitResultResponseBodyAdvice implicitResultResponseBodyAdvice(MvcCommonHandlerProperties properties){
        return new ImplicitResultResponseBodyAdvice(properties) ;
    }

    @Bean
    public UnifiedExceptionHandler unifiedExceptionHandler(MvcCommonHandlerProperties properties){
        return new UnifiedExceptionHandler(properties) ;
    }
}
