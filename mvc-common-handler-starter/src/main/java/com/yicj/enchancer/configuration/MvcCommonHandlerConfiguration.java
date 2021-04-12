package com.yicj.enchancer.configuration;

import com.yicj.enchancer.servlet.mvc.component.ImplicitResultResponseBodyAdvice;
import com.yicj.enchancer.servlet.mvc.component.UnifiedExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MvcCommonHandlerConfiguration {

    @Bean
    public ImplicitResultResponseBodyAdvice implicitResultResponseBodyAdvice(){
        return new ImplicitResultResponseBodyAdvice() ;
    }

    @Bean
    public UnifiedExceptionHandler unifiedExceptionHandler(){
        return new UnifiedExceptionHandler() ;
    }
}
