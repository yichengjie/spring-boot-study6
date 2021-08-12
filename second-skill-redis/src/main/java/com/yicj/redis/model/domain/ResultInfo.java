package com.yicj.redis.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 公共返回对象
 * 返回说明
 */
@Data
public class ResultInfo<T> implements Serializable {
    //成功标识0=失败，1=成功
    private Integer code;
    //描述信息
    private String message;
    //访问路径
    private String path;
    //返回数据对象
    private T data;
}