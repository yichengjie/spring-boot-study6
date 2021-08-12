package com.yicj.redis.service;

import com.yicj.redis.mapper.SecKillVouchersMapper;
import com.yicj.redis.model.pojo.SecKillVouchers;
import com.yicj.redis.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀业务逻辑层
 */
@Service
public class SecKillService {
    
    @Resource
    private SecKillVouchersMapper secKillVouchersMapper;

    /**
     * 添加需要抢购的代金券
     * @param seckillVouchers
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSecKillVouchers(SecKillVouchers seckillVouchers) {
        // 非空校验
        AssertUtil.isTrue(seckillVouchers.getFkVoucherId() == null, "请选择需要抢购的代金券");
        AssertUtil.isTrue(seckillVouchers.getAmount() == 0, "请输入抢购总数量");
        Date now = new Date();
        AssertUtil.isNotNull(seckillVouchers.getStartTime(), "请输入开始时间");
        // 生产环境下面一行代码需放行，这里注释方便测试
        // AssertUtil.isTrue(now.after(seckillVouchers.getStartTime()), "开始时间不能早于当前时间");
        AssertUtil.isNotNull(seckillVouchers.getEndTime(), "请输入结束时间");
        AssertUtil.isTrue(now.after(seckillVouchers.getEndTime()), "结束时间不能早于当前时间");
        AssertUtil.isTrue(seckillVouchers.getStartTime().after(seckillVouchers.getEndTime()), "开始时间不能晚于结束时间");

        // 验证数据库中是否已经存在该券的秒杀活动
        SecKillVouchers secKillVouchersFromDb = secKillVouchersMapper.selectVoucher(seckillVouchers.getFkVoucherId());
        AssertUtil.isTrue(secKillVouchersFromDb != null, "该券已经拥有了抢购活动");
        // 插入数据库
        secKillVouchersMapper.save(seckillVouchers);
    }

}