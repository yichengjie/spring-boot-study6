package com.yicj.study.mvc.aop;

import com.yicj.study.importselector.HttpServer;
import com.yicj.study.importselector.Server;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Server){
            MethodBeforeAdvice advice = (method, objects, obj) ->{
                log.info("---- before method ...");
            } ;
            return createProxy(bean, advice) ;
        }
        return bean ;
    }

    private Object createProxy(Object bean, Advice advice){
        ProxyFactory proxyFactory = new ProxyFactory() ;
        proxyFactory.setInterfaces(bean.getClass().getInterfaces());
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice(advice);
        return proxyFactory.getProxy();
    }

    public static void main(String[] args) {
        Server server = new HttpServer() ;
        ServerBeanPostProcessor processor = new ServerBeanPostProcessor();
        MethodBeforeAdvice advice = (method, objects, obj) ->{
            System.out.println("------ before method ...");
        } ;
        Server serverProxy = (Server)processor.createProxy(server, advice);
        serverProxy.start();
    }
}
