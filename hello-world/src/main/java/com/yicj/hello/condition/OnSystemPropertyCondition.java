package com.yicj.hello.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;


public class OnSystemPropertyCondition  implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes =
                metadata.getAnnotationAttributes(ConditionalOnSystemProperty.class.getName());
        String propertyName = (String) attributes.get("name") ;
        String propertyValue = (String) attributes.get("value") ;
        String systemPropertyValue = System.getProperty(propertyName) ;
        return propertyValue.equalsIgnoreCase(systemPropertyValue);
    }
}
