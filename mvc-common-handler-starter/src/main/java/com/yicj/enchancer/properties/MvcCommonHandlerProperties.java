package com.yicj.enchancer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mvc.common.handler")
public class MvcCommonHandlerProperties {
    private ResultProperties result = new ResultProperties() ;
}
