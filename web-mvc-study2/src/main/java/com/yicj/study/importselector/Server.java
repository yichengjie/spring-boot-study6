package com.yicj.study.importselector;

public interface Server {
    void start() ;
    void stop() ;
    enum Type{
        HTTP,
        FTP
    }
}
