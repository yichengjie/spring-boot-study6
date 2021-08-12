package com.yicj.redis.model.pojo;

import com.yicj.redis.model.base.BaseModel;
import lombok.Data;

@Data
//代金券订单信息
public class VoucherOrders extends BaseModel {

    //订单编号
    private String orderNo;
    //代金券
    private Integer fkVoucherId;
    //下单用户
    private Integer fkDinerId;
    //生成qrcode
    private String qrcode;
    //支付方式 0=微信支付 1=支付宝
    private int payment;
    //订单状态 -1=已取消 0=未支付 1=已支付 2=已消费 3=已过期
    private int status;
    //订单类型 0=正常订单 1=抢购订单
    private int orderType;
    //抢购订单的外键
    private int fkSecKillId;
}