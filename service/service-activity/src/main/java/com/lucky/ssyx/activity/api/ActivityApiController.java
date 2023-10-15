package com.lucky.ssyx.activity.api;

import com.lucky.ssyx.activity.service.ActivityInfoService;
import com.lucky.ssyx.activity.service.CouponInfoService;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.vo.order.CartInfoVo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/26
 */
@Api(tags = "营销与优惠券接口")
@RestController
@RequestMapping("/api/activity")
@Slf4j
public class ActivityApiController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation(value = "根据skuId列表获取营销销信息")
    @PostMapping("/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList){
        Map<Long,List<String>> resultMap = activityInfoService.findActivity(skuIdList);
        return resultMap;
    }

    @ApiOperation("根据skuId获取营销与优惠券信息")
    @GetMapping("/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String,Object> findActivityAndCoupon(@PathVariable Long skuId,@PathVariable Long userId){
        Map<String,Object> map =  activityInfoService.findActivityAndCoupon(skuId,userId);
        return map;
    }

    @ApiOperation("获取购物车满足条件的营销与优惠券信息")
    @PostMapping("/inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId) {
        OrderConfirmVo orderConfirmVo =  activityInfoService.findCartActivityAndCoupon(cartInfoList, userId);
        return orderConfirmVo;
    }

    @ApiOperation("获取购物车营销规则数据")
    @PostMapping("/inner/findCartActivityList")
    public List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList){
        List<CartInfoVo> cartActivityList = activityInfoService.findCartActivityList(cartInfoList);
        return cartActivityList;
    }

    @ApiOperation("获取购物车对应的优惠卷")
    @PostMapping("/inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList,@PathVariable Long couponId){
        CouponInfo couponInfo =  couponInfoService.findRangeSkuIdList(cartInfoList,couponId);
        return couponInfo;
    }

    @ApiOperation("更新优惠券使用状态")
    @GetMapping("/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public void updateCouponInfoUseStatus(@PathVariable Long couponId,@PathVariable Long userId,@PathVariable Long orderId) {
         couponInfoService.updateCouponInfoUseStatus(couponId,userId,orderId);
    }
}
