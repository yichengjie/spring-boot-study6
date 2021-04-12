package com.yicj.study.mvc.model;

import lombok.Data;

@Data
public class JsonResult {
    private int code;
    private String msg;
    private Object obj;
    public JsonResult(){}
    public JsonResult(int code ,String msg){
        this.code=code;
        this.msg=msg;
    }
    public JsonResult(int code ,String msg,Object obj){
        this.code=code;
        this.msg=msg;
        this.obj=obj;
    }
}