package com.yicj.study.netty.model;

import com.yicj.study.netty.dispatcher.Message;

public class HeartbeatResponse implements Message {
    public static final String TYPE = "Heartbeat";
}
