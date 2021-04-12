package com.yicj.enchancer.annotation;

import com.yicj.enchancer.servlet.mvc.component.ImplicitResultResponseBodyAdvice;
import com.yicj.enchancer.servlet.mvc.component.UnifiedExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(value = {
        ImplicitResultResponseBodyAdvice.class, UnifiedExceptionHandler.class
})
@EnableMvcCommonHandler
public class MvcCommonHandlerAutoConfiguration {

}
