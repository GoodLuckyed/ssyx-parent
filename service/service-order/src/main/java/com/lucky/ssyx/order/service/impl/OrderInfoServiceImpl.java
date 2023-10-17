package com.lucky.ssyx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.ActivityFeginClient;
import com.lucky.ssyx.cart.CartFeignClient;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.constant.MqConst;
import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.common.exception.SsyxException;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.common.service.RabbitService;
import com.lucky.ssyx.common.utils.DateUtil;
import com.lucky.ssyx.enums.*;
import com.lucky.ssyx.model.activity.ActivityRule;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.model.order.OrderItem;
import com.lucky.ssyx.order.mapper.OrderInfoMapper;
import com.lucky.ssyx.order.mapper.OrderItemMapper;
import com.lucky.ssyx.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.order.service.OrderItemService;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.user.UserFeginClient;
import com.lucky.ssyx.vo.order.CartInfoVo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import com.lucky.ssyx.vo.order.OrderSubmitVo;
import com.lucky.ssyx.vo.order.OrderUserQueryVo;
import com.lucky.ssyx.vo.product.SkuStockLockVo;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-10-12
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private UserFeginClient userFeginClient;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ActivityFeginClient activityFeginClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RabbitService rabbitService;

    /**
     * 确认订单
     *
     * @return
     */
    @Override
    public OrderConfirmVo confirm() {
        //获取用户id
        Long userId = AuthContextHolder.getUserId();
        //根据用户id获取对应的团长和提货点信息
        LeaderAddressVo leaderAddressVo = userFeginClient.getUserAddressByUserId(userId);
        //获取用户在购物车里选中的购物项列表
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);
        //生成订单唯一标识
        String orderNo = System.currentTimeMillis() + "";
        redisTemplate.opsForValue().set(RedisConst.ORDER_REPEAT + orderNo, orderNo, 24, TimeUnit.HOURS);
        //获取购物车满足条件的营销与优惠券信息
        OrderConfirmVo orderConfirmVo = activityFeginClient.findCartActivityAndCoupon(cartCheckedList, userId);
        //封装数据返回
        orderConfirmVo.setOrderNo(orderNo);
        orderConfirmVo.setLeaderAddressVo(leaderAddressVo);
        return orderConfirmVo;
    }

    /**
     * 生成订单
     *
     * @param orderSubmitVo
     * @return
     */
    @Override
    public Long submitOrder(OrderSubmitVo orderSubmitVo) {
        //1.设置要给哪个用户生成订单
        Long userId = AuthContextHolder.getUserId();
        orderSubmitVo.setUserId(userId);
        //2.校验订单不能重复,(redis + lua脚本)
        String orderNo = orderSubmitVo.getOrderNo();
        if (StringUtils.isEmpty(orderNo)) {
            throw new SsyxException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        String script = "if(redis.call('get', KEYS[1]) == ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end";
        Boolean flag = (Boolean) redisTemplate.execute(new DefaultRedisScript(script, Boolean.class), Arrays.asList(RedisConst.ORDER_REPEAT + orderNo), orderNo);
        if (!flag) {
            throw new SsyxException(ResultCodeEnum.REPEAT_SUBMIT);
        }

        //3.验证库存和锁定库存
        //远程调用service-cart获取购物车中选中的商品
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);
        //普通商品处理
        List<CartInfo> commonSkuList = cartCheckedList.stream().
                filter(cartInfo -> cartInfo.getSkuType() == SkuType.COMMON.getCode())
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(commonSkuList)) {
            List<SkuStockLockVo> commonStockLockVoList = commonSkuList.stream().map(item -> {
                SkuStockLockVo skuStockLockVo = new SkuStockLockVo();
                skuStockLockVo.setSkuId(item.getSkuId());
                skuStockLockVo.setSkuNum(item.getSkuNum());
                return skuStockLockVo;
            }).collect(Collectors.toList());
            //远程调用service-product模块,验证和锁定库存
            Boolean isLockSuccess = productFeignClient.checkAndLock(commonStockLockVoList, orderNo);
            if (!isLockSuccess) {
                throw new SsyxException(ResultCodeEnum.ORDER_STOCK_FALL);
            }
        }

        //4.保存订单，向数据库插入数据
        Long orderId = orderInfoService.saveOrder(orderSubmitVo, cartCheckedList);

        return orderId;
    }

    //保存订单
    @Transactional(rollbackFor = { Exception.class })
    @Override
    public Long saveOrder(OrderSubmitVo orderSubmitVo, List<CartInfo> cartCheckedList) {
        //获取用户id
        Long userId = AuthContextHolder.getUserId();
        //判断cartCheckedList是否为空
        if (CollectionUtils.isEmpty(cartCheckedList)) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        //根据用户id获取对应的团长和提货点信息
        LeaderAddressVo leaderAddressVo = userFeginClient.getUserAddressByUserId(userId);
        if (leaderAddressVo == null) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        //计算营销活动金额
        Map<String, BigDecimal> activitySplitAmountMap = this.computeActivitySplitAmount(cartCheckedList);
        //计算优惠卷优惠金额
        Map<String, BigDecimal> couponInfoSplitAmountMap = this.computeCouponInfoSplitAmount(cartCheckedList, orderSubmitVo.getCouponId());

        //添加数据到order_item表里
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo cartInfo : cartCheckedList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCategoryId(cartInfo.getCategoryId());
            if(cartInfo.getSkuType() == SkuType.COMMON.getCode()) {
                orderItem.setSkuType(SkuType.COMMON);
            } else {
                orderItem.setSkuType(SkuType.SECKILL);
            }
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setImgUrl(cartInfo.getImgUrl());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setLeaderId(orderSubmitVo.getLeaderId());

            //促销活动分摊金额
            BigDecimal splitActivityAmount = activitySplitAmountMap.get("activity:"+orderItem.getSkuId());
            if(null == splitActivityAmount) {
                splitActivityAmount = new BigDecimal(0);
            }
            orderItem.setSplitActivityAmount(splitActivityAmount);

            //优惠券分摊金额
            BigDecimal splitCouponAmount = couponInfoSplitAmountMap.get("coupon:"+orderItem.getSkuId());
            if(null == splitCouponAmount) {
                splitCouponAmount = new BigDecimal(0);
            }
            orderItem.setSplitCouponAmount(splitCouponAmount);

            //计算优惠后的总金额
            BigDecimal skuTotalAmount  = orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum()));
            BigDecimal splitTotalAmount = skuTotalAmount.subtract(splitActivityAmount).subtract(splitCouponAmount);
            orderItem.setSplitTotalAmount(splitTotalAmount);
            orderItemList.add(orderItem);
        }

        //添加数据到order_info表里
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setOrderNo(orderSubmitVo.getOrderNo());
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfo.setCouponId(orderSubmitVo.getCouponId());
        orderInfo.setLeaderId(orderSubmitVo.getLeaderId());
        orderInfo.setLeaderName(leaderAddressVo.getLeaderName());
        orderInfo.setLeaderPhone(leaderAddressVo.getLeaderPhone());
        orderInfo.setTakeName(leaderAddressVo.getTakeName());
        orderInfo.setReceiverName(orderSubmitVo.getReceiverName());
        orderInfo.setReceiverPhone(orderSubmitVo.getReceiverPhone());
        orderInfo.setReceiverProvince(leaderAddressVo.getProvince());
        orderInfo.setReceiverCity(leaderAddressVo.getCity());
        orderInfo.setReceiverDistrict(leaderAddressVo.getDistrict());
        orderInfo.setReceiverAddress(leaderAddressVo.getDetailAddress());
        orderInfo.setWareId(leaderAddressVo.getWareId());
        //计算总金额
        BigDecimal originalTotalAmount = this.computeTotalAmount(cartCheckedList);
        BigDecimal activityAmount = activitySplitAmountMap.get("activity:total");
        if(null == activityAmount){
            activityAmount = new BigDecimal(0);
        }
        BigDecimal couponAmount = couponInfoSplitAmountMap.get("coupon:total");
        if(null == couponAmount){
            couponAmount = new BigDecimal(0);
        }
        BigDecimal totalAmount = originalTotalAmount.subtract(activityAmount).subtract(couponAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setActivityAmount(activityAmount);
        orderInfo.setCouponAmount(couponAmount);
        orderInfo.setOriginalTotalAmount(originalTotalAmount);

        //计算团长佣金
        BigDecimal profitRate = new BigDecimal("0.2");
        BigDecimal commissionAmount = orderInfo.getTotalAmount().multiply(profitRate);
        orderInfo.setCommissionAmount(commissionAmount);

        //向数据库插入数据
        orderInfoMapper.insert(orderInfo);
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
        }
        orderItemService.saveBatch(orderItemList);

        //更新优惠券使用状态
        if (orderInfo.getCouponId() != null){
            activityFeginClient.updateCouponInfoUseStatus(orderInfo.getCouponId(),userId,orderInfo.getId());
        }

        //更新redis,记录用户购买个数
        String orderSkuKey = RedisConst.ORDER_SKU_MAP + orderSubmitVo.getUserId();
        BoundHashOperations<String, String, Integer> hashOperations = redisTemplate.boundHashOps(orderSkuKey);
        cartCheckedList.forEach(cartInfo -> {
            if(hashOperations.hasKey(cartInfo.getSkuId().toString())) {
                Integer orderSkuNum = hashOperations.get(cartInfo.getSkuId().toString()) + cartInfo.getSkuNum();
                hashOperations.put(cartInfo.getSkuId().toString(), orderSkuNum);
            }
        });
        redisTemplate.expire(orderSkuKey, DateUtil.getCurrentExpireTimes(), TimeUnit.SECONDS);

        return orderInfo.getId();
    }

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId,orderId);
        List<OrderItem> orderItemList = orderItemService.list(queryWrapper);
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    /**
     * 根据orderNo获取订单信息
     * @param orderNo
     * @return
     */
    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo,orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        return orderInfo;
    }

    /**
     * 修改订单的支付状态
     * @param orderNo
     */
    @Override
    public void updateOrderPayStatus(String orderNo) {
        OrderInfo orderInfo = this.getOrderInfoByOrderNo(orderNo);
        if (null == orderInfo || orderInfo.getOrderStatus() != OrderStatus.UNPAID){
            return;
        }
        //修改支付状态
        orderInfo.setOrderStatus(OrderStatus.WAITING_TAKE);
        orderInfo.setProcessStatus(ProcessStatus.WAITING_DELEVER);
        orderInfoMapper.updateById(orderInfo);
        //扣减库存
        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT,MqConst.ROUTING_MINUS_STOCK,orderNo);
    }

    /**
     * 查询用户不同类型的订单
     * @param pageParam
     * @param orderUserQueryVo
     * @return
     */
    @Override
    public IPage<OrderInfo> findUserOrderPage(Page<OrderInfo> pageParam, OrderUserQueryVo orderUserQueryVo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId,orderUserQueryVo.getUserId());
        wrapper.eq(OrderInfo::getOrderStatus,orderUserQueryVo.getOrderStatus());
        Page<OrderInfo> orderInfoPage = orderInfoMapper.selectPage(pageParam, wrapper);
        //获取每个订单，把每个订单里面订单项查询封装
        List<OrderInfo> records = orderInfoPage.getRecords();
        for (OrderInfo orderInfo : records) {
            LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderItem::getOrderId,orderInfo.getId());
            List<OrderItem> orderItemList = orderItemService.list(queryWrapper);
            //把订单项集合封装到每个订单里面
            orderInfo.setOrderItemList(orderItemList);
            //封装订单状态名称
            orderInfo.getParam().put("orderStatusName",orderInfo.getOrderStatus().getComment());
        }
        return orderInfoPage;
    }

    //计算总金额
    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal(0);
        for (CartInfo cartInfo : cartInfoList) {
            BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
            total = total.add(itemTotal);
        }
        return total;
    }

    /**
     * 计算购物项分摊的优惠减少金额
     * 打折：按折扣分担
     * 现金：按比例分摊
     *
     * @param cartInfoParamList
     * @return
     */
    private Map<String, BigDecimal> computeActivitySplitAmount(List<CartInfo> cartInfoParamList) {
        Map<String, BigDecimal> activitySplitAmountMap = new HashMap<>();

        //促销活动相关信息
        List<CartInfoVo> cartInfoVoList = activityFeginClient.findCartActivityList(cartInfoParamList);

        //活动总金额
        BigDecimal activityReduceAmount = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(cartInfoVoList)) {
            for (CartInfoVo cartInfoVo : cartInfoVoList) {
                ActivityRule activityRule = cartInfoVo.getActivityRule();
                List<CartInfo> cartInfoList = cartInfoVo.getCartInfoList();
                if (null != activityRule) {
                    //优惠金额， 按比例分摊
                    BigDecimal reduceAmount = activityRule.getReduceAmount();
                    activityReduceAmount = activityReduceAmount.add(reduceAmount);
                    if (cartInfoList.size() == 1) {
                        activitySplitAmountMap.put("activity:" + cartInfoList.get(0).getSkuId(), reduceAmount);
                    } else {
                        //总金额
                        BigDecimal originalTotalAmount = new BigDecimal(0);
                        for (CartInfo cartInfo : cartInfoList) {
                            BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                            originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                        }
                        //记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
                        BigDecimal skuPartReduceAmount = new BigDecimal(0);
                        if (activityRule.getActivityType() == ActivityType.FULL_REDUCTION) {
                            for (int i = 0, len = cartInfoList.size(); i < len; i++) {
                                CartInfo cartInfo = cartInfoList.get(i);
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                                    //sku分摊金额
                                    BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);
                                }
                            }
                        } else {
                            for (int i = 0, len = cartInfoList.size(); i < len; i++) {
                                CartInfo cartInfo = cartInfoList.get(i);
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));

                                    //sku分摊金额
                                    BigDecimal skuDiscountTotalAmount = skuTotalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                                    BigDecimal skuReduceAmount = skuTotalAmount.subtract(skuDiscountTotalAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
        activitySplitAmountMap.put("activity:total", activityReduceAmount);
        return activitySplitAmountMap;
    }

    //优惠卷优惠金额
    private Map<String, BigDecimal> computeCouponInfoSplitAmount(List<CartInfo> cartInfoList, Long couponId) {
        Map<String, BigDecimal> couponInfoSplitAmountMap = new HashMap<>();

        if (null == couponId) return couponInfoSplitAmountMap;
        CouponInfo couponInfo = activityFeginClient.findRangeSkuIdList(cartInfoList, couponId);

        if (null != couponInfo) {
            //sku对应的订单明细
            Map<Long, CartInfo> skuIdToCartInfoMap = new HashMap<>();
            for (CartInfo cartInfo : cartInfoList) {
                skuIdToCartInfoMap.put(cartInfo.getSkuId(), cartInfo);
            }
            //优惠券对应的skuId列表
            List<Long> skuIdList = couponInfo.getSkuIdList();
            if (CollectionUtils.isEmpty(skuIdList)) {
                return couponInfoSplitAmountMap;
            }
            //优惠券优化总金额
            BigDecimal reduceAmount = couponInfo.getAmount();
            if (skuIdList.size() == 1) {
                //sku的优化金额
                couponInfoSplitAmountMap.put("coupon:" + skuIdToCartInfoMap.get(skuIdList.get(0)).getSkuId(), reduceAmount);
            } else {
                //总金额
                BigDecimal originalTotalAmount = new BigDecimal(0);
                for (Long skuId : skuIdList) {
                    CartInfo cartInfo = skuIdToCartInfoMap.get(skuId);
                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                    originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                }
                //记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
                BigDecimal skuPartReduceAmount = new BigDecimal(0);
                if (couponInfo.getCouponType() == CouponType.CASH || couponInfo.getCouponType() == CouponType.FULL_REDUCTION) {
                    for (int i = 0, len = skuIdList.size(); i < len; i++) {
                        CartInfo cartInfo = skuIdToCartInfoMap.get(skuIdList.get(i));
                        if (i < len - 1) {
                            BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                            //sku分摊金额
                            BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                            couponInfoSplitAmountMap.put("coupon:" + cartInfo.getSkuId(), skuReduceAmount);

                            skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                        } else {
                            BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                            couponInfoSplitAmountMap.put("coupon:" + cartInfo.getSkuId(), skuReduceAmount);
                        }
                    }
                }
            }
            couponInfoSplitAmountMap.put("coupon:total", couponInfo.getAmount());
        }
        return couponInfoSplitAmountMap;
    }
}












