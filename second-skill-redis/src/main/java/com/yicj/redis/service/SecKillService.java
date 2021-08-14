package com.yicj.redis.service;

import cn.hutool.core.util.IdUtil;
import com.yicj.redis.mapper.SecKillVouchersMapper;
import com.yicj.redis.mapper.VoucherOrdersMapper;
import com.yicj.redis.model.domain.ResultInfo;
import com.yicj.redis.model.pojo.SecKillVouchers;
import com.yicj.redis.model.pojo.VoucherOrders;
import com.yicj.redis.utils.AssertUtil;
import com.yicj.redis.utils.ResultInfoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀业务逻辑层
 */
@Service
public class SecKillService {
    
    @Resource
    private SecKillVouchersMapper secKillVouchersMapper;
    @Resource
    private VoucherOrdersMapper voucherOrdersMapper;
    @Resource
    private RestTemplate restTemplate;

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


    /**
     * 抢购代金券
     *
     * @param voucherId   代金券 ID
     * @param accessToken 登录token
     * @Para path 访问路径
     */
    public ResultInfo doSecKill(Integer voucherId, String accessToken, String path) {
        // 基本参数校验
        AssertUtil.isTrue(voucherId == null || voucherId < 0, "请选择需要抢购的代金券");
        AssertUtil.isNotEmpty(accessToken, "请登录");
        // 判断此代金券是否加入抢购
        SecKillVouchers seckillVouchers = secKillVouchersMapper.selectVoucher(voucherId);
        AssertUtil.isTrue(seckillVouchers == null, "该代金券并未有抢购活动");
        // 判断是否有效
        AssertUtil.isTrue(seckillVouchers.getIsValid() == 0, "该活动已结束");
        // 判断是否开始、结束
        Date now = new Date();
        AssertUtil.isTrue(now.before(seckillVouchers.getStartTime()), "该抢购还未开始");
        AssertUtil.isTrue(now.after(seckillVouchers.getEndTime()), "该抢购已结束");
        // 判断是否卖完
        AssertUtil.isTrue(seckillVouchers.getAmount() < 1, "该券已经卖完了");
        // 获取登录用户信息
        // 这里的data是一个LinkedHashMap，SignInDinerInfo
        // 这里的data是一个LinkedHashMap，SignInDinerInfo
        Integer userId = parseUserIdByAccessToken(accessToken) ;
        // 判断登录用户是否已抢到(一个用户针对这次活动只能买一次)
        VoucherOrders order = voucherOrdersMapper.findDinerOrder(userId, seckillVouchers.getId());
        AssertUtil.isTrue(order != null, "该用户已抢到该代金券，无需再抢");
        // 扣库存
        int count = secKillVouchersMapper.stockDecrease(seckillVouchers.getId());
        AssertUtil.isTrue(count == 0, "该券已经卖完了");
        // 下单
        VoucherOrders voucherOrders = new VoucherOrders();
        voucherOrders.setFkDinerId(userId);
        voucherOrders.setFkSecKillId(seckillVouchers.getId());
        voucherOrders.setFkVoucherId(seckillVouchers.getFkVoucherId());
        String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();
        voucherOrders.setOrderNo(orderNo);
        voucherOrders.setOrderType(1);
        voucherOrders.setStatus(0);
        count = voucherOrdersMapper.save(voucherOrders);
        AssertUtil.isTrue(count == 0, "用户抢购失败");
        return ResultInfoUtil.buildSuccess(path, "抢购成功");
    }


    private Integer parseUserIdByAccessToken(String accessToken){

        return accessToken.hashCode() ;
    }

}