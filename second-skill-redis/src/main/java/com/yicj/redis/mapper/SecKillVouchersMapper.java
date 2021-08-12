package com.yicj.redis.mapper;

import com.yicj.redis.model.pojo.SecKillVouchers;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 秒杀代金券 Mapper
 */
public interface SecKillVouchersMapper {

    // 新增秒杀活动
    @Insert("insert into t_seckill_vouchers (fk_voucher_id, amount, start_time, end_time, is_valid, create_date, update_date) " +
            " values (#{fkVoucherId}, #{amount}, #{startTime}, #{endTime}, 1, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(SecKillVouchers seckillVouchers);
    
    // 根据代金券 ID 查询该代金券是否参与抢购活动
    @Select("select id, fk_voucher_id, amount, start_time, end_time, is_valid " +
            " from t_seckill_vouchers where fk_voucher_id = #{voucherId}")
    SecKillVouchers selectVoucher(Integer voucherId);

}