package com.yicj.redis.controller;

import com.yicj.redis.model.domain.ResultInfo;
import com.yicj.redis.model.pojo.SecKillVouchers;
import com.yicj.redis.service.SecKillService;
import com.yicj.redis.utils.ResultInfoUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 秒杀控制层
 */
@RestController
public class SecKillController {

    @Resource
    private SecKillService seckillService;
    @Resource
    private HttpServletRequest request;

    /**
     * 新增秒杀活动
     * @param secKillVouchers
     * @return
     */
    @PostMapping("add")
    public ResultInfo<String> addSecKillVouchers(@RequestBody SecKillVouchers secKillVouchers) {
        seckillService.addSecKillVouchers(secKillVouchers);
        return ResultInfoUtil.buildSuccess(request.getServletPath(),
                "添加成功");
    }

}