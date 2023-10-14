package com.lucky.ssyx.order.service.impl;

import com.lucky.ssyx.activity.ActivityFeginClient;
import com.lucky.ssyx.cart.CartFeignClient;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.order.mapper.OrderInfoMapper;
import com.lucky.ssyx.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.user.UserFeginClient;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    /**
     * 确认订单
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
        redisTemplate.opsForValue().set(RedisConst.ORDER_REPEAT + orderNo,orderNo,24, TimeUnit.HOURS);
        //获取购物车满足条件的营销与优惠券信息
        OrderConfirmVo orderConfirmVo = activityFeginClient.findCartActivityAndCoupon(cartCheckedList, userId);
        //封装数据返回
        orderConfirmVo.setOrderNo(orderNo);
        orderConfirmVo.setLeaderAddressVo(leaderAddressVo);
        return orderConfirmVo;
    }
}












