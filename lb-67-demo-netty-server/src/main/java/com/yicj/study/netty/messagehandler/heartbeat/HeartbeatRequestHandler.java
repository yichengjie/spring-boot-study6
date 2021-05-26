package com.yicj.study.netty.messagehandler.heartbeat;

import com.yicj.study.netty.messagehandler.MessageHandler;
import com.yicj.study.netty.model.HeartbeatRequest;
import com.yicj.study.netty.model.HeartbeatResponse;
import com.yicj.study.netty.model.Invocation;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}