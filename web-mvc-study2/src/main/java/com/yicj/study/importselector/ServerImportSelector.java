package com.yicj.study.importselector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class ServerImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes =
                importingClassMetadata.getAnnotationAttributes(EnableServer.class.getName());
        Server.Type type = (Server.Type)attributes.get("type") ;
        String [] importClassNames = new String[0] ;
        switch (type){
            case HTTP:
                importClassNames = new String[]{HttpServer.class.getName()} ;
                break;
            case FTP:
                importClassNames = new String[]{FtpServer.class.getName()} ;
                break;
        }
        return importClassNames;
    }
}
