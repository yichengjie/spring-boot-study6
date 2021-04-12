package com.yicj.enchancer.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException {
    private int code = 500 ;
    public AppException(String msg){
       this(500, msg) ;
    }
    public AppException(int code,String msg){
        super(msg);
        this.code = code ;
    }
}
