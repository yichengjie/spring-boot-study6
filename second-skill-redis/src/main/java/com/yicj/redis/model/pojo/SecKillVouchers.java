package com.yicj.redis.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yicj.redis.model.base.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
//抢购代金券信息
public class SecKillVouchers extends BaseModel {

    //代金券外键
    private Integer fkVoucherId;
    //数量
    private int amount;
    //抢购开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    //抢购结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
}