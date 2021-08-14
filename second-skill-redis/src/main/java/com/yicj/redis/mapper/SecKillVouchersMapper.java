package com.yicj.redis.mapper;

import com.yicj.redis.model.pojo.SecKillVouchers;
import org.apache.ibatis.annotations.*;

/**
 * 秒杀代金券 Mapper
 */
public interface SecKillVouchersMapper {

    // 新增秒杀活动
    @Insert("insert into t_secKill_vouchers (fk_voucher_id, amount, start_time, end_time, is_valid, create_date, update_date) " +
            " values (#{fkVoucherId}, #{amount}, #{startTime}, #{endTime}, 1, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(SecKillVouchers seckillVouchers);
    
    // 根据代金券 ID 查询该代金券是否参与抢购活动
    @Select("select id, fk_voucher_id, amount, start_time, end_time, is_valid " +
            " from t_secKill_vouchers where fk_voucher_id = #{voucherId}")
    SecKillVouchers selectVoucher(Integer voucherId);


    // 减库存
    @Update("update t_secKill_vouchers set amount = amount - 1 " +
            " where id = #{secKillId}")
    int stockDecrease(@Param("secKillId") int secKillId);
}