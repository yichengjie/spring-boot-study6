-- 代金券表
CREATE TABLE `t_vouchers` (
`id`  int(10) NOT NULL AUTO_INCREMENT ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代金券标题' ,
`thumbnail`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图' ,
`amount`  int(11) NULL DEFAULT NULL COMMENT '抵扣金额' ,
`price`  decimal(10,2) NULL DEFAULT NULL COMMENT '售价' ,
`status`  int(10) NULL DEFAULT NULL COMMENT '-1=过期 0=下架 1=上架' ,
`start_use_time`  datetime NULL DEFAULT NULL COMMENT '开始使用时间' ,
`expire_time`  datetime NULL DEFAULT NULL COMMENT '过期时间' ,
`redeem_restaurant_id`  int(10) NULL DEFAULT NULL COMMENT '验证餐厅' ,
`stock`  int(11) NULL DEFAULT 0 COMMENT '库存' ,
`stock_left`  int(11) NULL DEFAULT 0 COMMENT '剩余数量' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息' ,
`clause`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用条款' ,
`create_date`  datetime NULL DEFAULT NULL ,
`update_date`  datetime NULL DEFAULT NULL ,
`is_valid`  tinyint(1) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;

-- 抢购活动表
CREATE TABLE `t_seckill_vouchers` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`fk_voucher_id`  int(11) NULL DEFAULT NULL ,
`amount`  int(11) NULL DEFAULT NULL ,
`start_time`  datetime NULL DEFAULT NULL ,
`end_time`  datetime NULL DEFAULT NULL ,
`is_valid`  int(11) NULL DEFAULT NULL ,
`create_date`  datetime NULL DEFAULT NULL ,
`update_date`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;

-- 订单表
CREATE TABLE `t_voucher_orders` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`order_no`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`fk_voucher_id`  int(11) NULL DEFAULT NULL ,
`fk_diner_id`  int(11) NULL DEFAULT NULL ,
`qrcode`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址' ,
`payment`  tinyint(4) NULL DEFAULT NULL COMMENT '0=微信支付 1=支付宝支付' ,
`status`  tinyint(1) NULL DEFAULT NULL COMMENT '订单状态：-1=已取消 0=未支付 1=已支付 2=已消费 3=已过期' ,
`fk_seckill_id`  int(11) NULL DEFAULT NULL COMMENT '如果是抢购订单时，抢购订单的id' ,
`order_type`  int(11) NULL DEFAULT NULL COMMENT '订单类型：0=正常订单 1=抢购订单' ,
`create_date`  datetime NULL DEFAULT NULL ,
`update_date`  datetime NULL DEFAULT NULL ,
`is_valid`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;