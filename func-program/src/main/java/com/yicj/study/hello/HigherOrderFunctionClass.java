package com.yicj.study.hello;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HigherOrderFunctionClass {

    interface IConfigurator<T> {
        void configure(T t) ;
    }

    interface IFactory<T> {
        T create() ;
    }

    interface IProducer<T> {
        T produce() ;
    }

    public <T> IFactory<T> createFactory(
        IProducer<T> producer, IConfigurator<T> configurator
    ){
        return () ->{
            T instance = producer.produce() ;
            configurator.configure(instance);
            return instance ;
        } ;
    }

    public static void main(String[] args) {
        HigherOrderFunctionClass higherOrderFunctionClass =
                new HigherOrderFunctionClass() ;
        IFactory<User> factory = higherOrderFunctionClass.createFactory(
                () -> User.builder().id(100).name("test").build(),
                (user) -> {
                    log.info("用户信息:{}", user);
                    user.setMobile("1234567");
                }
        );

        User user = factory.create();
        log.info("test: {}", user);
    }
}
