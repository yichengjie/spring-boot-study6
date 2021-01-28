package com.yicj.study.mvc.model;

import lombok.Data;

@Data
public class JsonResult<T> {
    private Integer code ;
    private String msg ;
    private T data ;

    public static <T> JsonResult<T> success(T data){
        JsonResult<T> result = new JsonResult<>();
        result.code = 200 ;
        result.setMsg("success");
        result.setData(data);
        return result ;
    }

    public static <T> JsonResult<T> error(String msg){
        JsonResult<T> result = new JsonResult<>();
        result.code = 500 ;
        result.setMsg(msg);
        return result ;
    }
}
