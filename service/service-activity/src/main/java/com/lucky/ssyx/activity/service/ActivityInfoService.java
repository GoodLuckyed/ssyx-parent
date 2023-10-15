package com.lucky.ssyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.activity.ActivityInfo;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.vo.activity.ActivityRuleVo;
import com.lucky.ssyx.vo.order.CartInfoVo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    /**
     * 获取营销活动信息分页列表
     * @param pageObj
     */
    IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageObj);

    /**
     * 根据营销活动id查询营销规则数据
     * @param activityId
     */
    Map<String,Object> findActivityRuleList(Long activityId);

    /**
     * 新增营销活动规则
     * @param activityRuleVo
     */
    void saveActivityRule(ActivityRuleVo activityRuleVo);

    /**
     * 根据关键字获取sku列表
     * @param keyword
     */
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 根据skuId列表获取营销销信息
     * @param skuIdList
     * @return
     */
    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    /**
     * 根据skuId获取营销与优惠券信息
     * @param skuId
     * @param userId
     * @return
     */
    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    /**
     * 获取购物车满足条件的营销与优惠券信息
     * @param cartInfoList
     * @param userId
     * @return
     */
    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);

    /**
     * 获取购物车营销规则数据
     * @param cartInfoList
     * @return
     */
    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);
}
