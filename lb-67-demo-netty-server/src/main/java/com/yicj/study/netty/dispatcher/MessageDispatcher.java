package com.yicj.study.netty.dispatcher;

import com.alibaba.fastjson.JSON;
import com.yicj.study.netty.messagehandler.MessageHandler;
import com.yicj.study.netty.model.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 消息分发
@Component
@ChannelHandler.Sharable // 标记这个 ChannelHandler 可以被多个 Channel 使用
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

    private final ExecutorService executor =  Executors.newFixedThreadPool(200);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) {
        // <3.1> 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());
        // 获得  MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // <3.2> 解析消息
        Message message = JSON.parseObject(invocation.getMessage(), messageClass);
        // <3.3> 执行逻辑
        executor.submit(() -> {
            // noinspection unchecked
            messageHandler.execute(ctx.channel(), message);
        });
    }
}