package com.lucky.ssyx.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import com.lucky.ssyx.vo.order.OrderSubmitVo;

import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-10-12
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 确认订单
     * @return
     */
    OrderConfirmVo confirm();

    /**
     * 生成订单
     * @param orderSubmitVo
     * @return
     */
    Long submitOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 保存订单
     * @param orderSubmitVo
     * @param cartCheckedList
     * @return
     */
    Long saveOrder(OrderSubmitVo orderSubmitVo, List<CartInfo> cartCheckedList);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfoById(Long orderId);

    /**
     * 根据orderNo获取订单信息
     * @param orderNo
     * @return
     */
    OrderInfo getOrderInfoByOrderNo(String orderNo);

    /**
     * 修改订单的支付状态
     * @param orderNo
     */
    void updateOrderPayStatus(String orderNo);
}
