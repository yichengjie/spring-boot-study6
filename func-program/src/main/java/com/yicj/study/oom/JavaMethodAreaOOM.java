package com.yicj.study.oom;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * -verbose:gc -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\\tmp\\
 * -XX:MetaspaceSize=200m;-XX:MaxMetaspaceSize=256m;
 */
public class JavaMethodAreaOOM {

    public static void main(String[] args) {
        while (true){
            Enhancer enhancer = new Enhancer() ;
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o, args);
                }
            });
            enhancer.create();
        }
    }

    static class OOMObject{}
}
