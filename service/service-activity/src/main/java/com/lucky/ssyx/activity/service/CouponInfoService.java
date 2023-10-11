package com.lucky.ssyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.vo.activity.CouponRuleVo;

import java.util.List;
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

    /**
     * 根据skuId+userId获取优惠卷信息
     * @param skuId
     * @param userId
     * @return
     */
    List<CouponInfo> findCouponInfoList(Long skuId, Long userId);

    /**
     * 获取购物车可以使用的优惠卷列表
     * @param cartInfoList
     * @param userId
     * @return
     */
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);
}
