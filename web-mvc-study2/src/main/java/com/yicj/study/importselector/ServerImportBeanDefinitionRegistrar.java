package com.yicj.study.importselector;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.Stream;

public class ServerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 复用{@link ServerImportSelector} 实现，避免重复劳动
        ImportSelector importSelector = new ServerImportSelector() ;
        String[] selectedClassNames = importSelector.selectImports(importingClassMetadata);
        //创建Bean的定义
        Stream.of(selectedClassNames)
                // 转化为BeanDefinitionBuilder对象
              .map(BeanDefinitionBuilder::genericBeanDefinition)
                //转化为BeanDefinition
              .map(BeanDefinitionBuilder::getBeanDefinition)
              .forEach(beanDefinition -> {
                  // 注册BeanDefinition到BeanDefinitionRegistry
                  BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition,registry) ;
              });
    }
}
