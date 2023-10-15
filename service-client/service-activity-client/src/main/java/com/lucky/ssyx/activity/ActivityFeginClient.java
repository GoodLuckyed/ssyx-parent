package com.lucky.ssyx.activity;

import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.vo.order.CartInfoVo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/26
 */
@FeignClient(value = "service-activity")
public interface ActivityFeginClient {
    /**
     * 根据skuId列表获取营销销信息
     * @param skuIdList
     * @return
     */
    @PostMapping("/api/activity/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    /**
     * 根据skuId获取营销与优惠券信息
     * @param skuId
     * @param userId
     * @return
     */
    @GetMapping("/api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String,Object> findActivityAndCoupon(@PathVariable Long skuId, @PathVariable Long userId);

    /**
     * 获取购物车满足条件的营销与优惠券信息
     * @param cartInfoList
     * @param userId
     * @return
     */
    @PostMapping("/api/activity/inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId);


    /**
     * 获取购物车营销规则数据
     * @param cartInfoList
     * @return
     */
    @PostMapping("/api/activity/inner/findCartActivityList")
    public List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList);

    /**
     * 获取购物车对应的优惠卷
     * @param cartInfoList
     * @param couponId
     * @return
     */
    @PostMapping("/api/activity/inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList, @PathVariable Long couponId);

    /**
     * 更新优惠券使用状态
     * @param couponId
     * @param userId
     * @param orderId
     */
    @GetMapping("/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public void updateCouponInfoUseStatus(@PathVariable Long couponId,@PathVariable Long userId,@PathVariable Long orderId);
}
