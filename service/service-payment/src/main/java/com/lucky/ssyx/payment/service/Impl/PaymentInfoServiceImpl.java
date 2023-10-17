package com.lucky.ssyx.payment.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.common.constant.MqConst;
import com.lucky.ssyx.common.exception.SsyxException;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.common.service.RabbitService;
import com.lucky.ssyx.enums.PaymentStatus;
import com.lucky.ssyx.enums.PaymentType;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.model.order.PaymentInfo;
import com.lucky.ssyx.order.OrderFeignClient;
import com.lucky.ssyx.payment.mapper.PaymentInfoMapper;
import com.lucky.ssyx.payment.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/16
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RabbitService rabbitService;

    /**
     * 根据orderNo判断支付记录表里是否有相同的记录
     * @param orderNo
     * @return
     */
    @Override
    public PaymentInfo getPaymentInfoByOrderNo(String orderNo) {
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderNo,orderNo);
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
        return paymentInfo;
    }

    /**
     * 保存支付信息
     * @param orderNo
     * @param paymentType
     */
    @Override
    public PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType) {
        //远程调用service-order模块,根据orderNo获取订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
        if (orderInfo == null){
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        //保存数据
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setTotalAmount(orderInfo.getTotalAmount());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
        String subject = "userId:" + orderInfo.getUserId() + "下订单";
        paymentInfo.setSubject(subject);

        paymentInfoMapper.insert(paymentInfo);
        return paymentInfo;
    }

    /**
     * 支付成功,更改支付记录的表的支付状态，订单信息表的订单状态
     * @param outTradeNo
     * @param resultMap
     */
    @Override
    public void paySuccess(String outTradeNo, Map<String, String> resultMap) {
        //查询支付记录表的支付状态,进行判断
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderNo,outTradeNo);
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
        if (paymentInfo.getPaymentStatus() != PaymentStatus.UNPAID){
            return;
        }
        //更改支付记录表的支付状态
        paymentInfo.setPaymentStatus(PaymentStatus.PAID);
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setCallbackContent(resultMap.toString());
        paymentInfoMapper.updateById(paymentInfo);
        //查询订单信息表的订单状态，更改为已支付.扣减库存
        rabbitService.sendMessage(MqConst.EXCHANGE_PAY_DIRECT,MqConst.ROUTING_PAY_SUCCESS,outTradeNo);
        //删除购物车中对应的记录
        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT,MqConst.ROUTING_DELETE_CART,paymentInfo.getUserId());
    }
}