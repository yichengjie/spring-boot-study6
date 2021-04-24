package com.yicj.study.netty.dispatcher;

import com.yicj.study.netty.messagehandler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class MessageHandlerContainer implements InitializingBean {
    // 消息类型与MessageHandler的映射
    private final Map<String, MessageHandler> handlers = new HashMap<>() ;
    @Autowired
    private ApplicationContext applicationContext ;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过ApplicationContext获取所有MessageHandler的Bean
        Collection<MessageHandler> values =
                applicationContext.getBeansOfType(MessageHandler.class).values();
        values.forEach(messageHandler ->handlers.put(messageHandler.getType(), messageHandler));
        // 添加到handlers中
        log.info("[afterPropertiesSet][消息处理器数量:{}]", handlers.size());
    }

    public MessageHandler getMessageHandler(String type){
        MessageHandler handler = handlers.get(type);
        if (handler == null){
            String format = String.format("类型(%s)找不到匹配的MessageHandler处理器", type);
            throw new IllegalArgumentException(format) ;
        }
        return handler ;
    }

    //获取MessageHandler处理的消息类
    public static Class<? extends Message> getMessageClass(MessageHandler handler){
        // 获取Bean对应的Class类名，因为有可能被Aop代理过
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        // 获取接口的Type数组
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();
        while ((Objects.isNull(interfaces)) || interfaces.length == 0
                &&Objects.nonNull(superclass)){// 此处以父类的接口为准
            interfaces = superclass.getGenericInterfaces() ;
            superclass = targetClass.getSuperclass() ;
        }
        if (Objects.nonNull(interfaces)){
            // 遍历interfaces数组
            for (Type type: interfaces){
                // 要求type是泛型参数
                if (type instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType)type ;
                    // 要求是MessageHandler接口
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)){
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        // 取首个元素
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length >0){
                            return (Class<Message>) actualTypeArguments[0] ;
                        }else {
                            String format = String.format("类型(%s)获取不到消息类型", handler);
                            throw new IllegalStateException(format) ;
                        }
                    }
                }
            }
        }
        String format = String.format("类型(%s)获取不到消息类型", handler);
        throw new IllegalStateException(format) ;
    }

}
