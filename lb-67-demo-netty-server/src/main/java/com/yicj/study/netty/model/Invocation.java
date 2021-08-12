package com.yicj.study.netty.model;

import com.alibaba.fastjson.JSON;
import com.yicj.study.netty.dispatcher.Message;
import lombok.Data;

/**
 * 通信协议的消息体
 */
@Data
public class Invocation {

    /**
     * 类型
     */
    private String type;
    /**
     * 消息，JSON 格式
     */
    private String message;

    // 空构造方法
    public Invocation() {
    }

    public Invocation(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }
    // ... 省略 setter、getter、toString 方法
}