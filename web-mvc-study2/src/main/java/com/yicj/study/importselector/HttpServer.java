package com.yicj.study.importselector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpServer implements Server {
    @Override
    public void start() {
        log.info("HTTP服务器启动中...");
    }

    @Override
    public void stop() {
        log.info("HTTP服务器关闭中...");
    }
}
