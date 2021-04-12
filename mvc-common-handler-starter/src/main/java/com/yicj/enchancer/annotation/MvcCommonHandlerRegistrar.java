package com.yicj.enchancer.annotation;

import com.yicj.enchancer.configuration.MvcCommonHandlerConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class MvcCommonHandlerRegistrar implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        AnnotationAttributes attributes =
                AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMvcCommonHandler.class.getName(), false)) ;
        if (attributes != null && attributes.getBoolean("value")) {
            return new String[]{MvcCommonHandlerConfiguration.class.getName()} ;
        }
        return new String[0];
    }
}
