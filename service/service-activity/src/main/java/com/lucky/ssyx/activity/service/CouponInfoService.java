package com.lucky.ssyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.vo.activity.CouponRuleVo;

import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
public interface CouponInfoService extends IService<CouponInfo> {

    /**
     * 获取优惠卷信息分页列表
     * @param pageObj
     */
    IPage<CouponInfo> selectPage(Page<CouponInfo> pageObj);

    /**
     * 根据优惠活动id查询优惠规则
     * @param id
     * @return
     */
    Map<String, Object> findCouponRuleListById(Long id);

    /**
     * 添加优惠规则
     * @param couponRuleVo
     */
    void saveCouponRule(CouponRuleVo couponRuleVo);
}
