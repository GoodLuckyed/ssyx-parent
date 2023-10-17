package com.lucky.ssyx.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.enums.PaymentType;
import com.lucky.ssyx.model.order.PaymentInfo;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/16
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 根据orderNo判断支付记录表里是否有相同的记录
     * @param orderNo
     * @return
     */
    PaymentInfo getPaymentInfoByOrderNo(String orderNo);

    /**
     * 保存支付信息
     * @param orderNo
     * @param paymentType
     */
    PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType);

    /**
     * 支付成功,更改支付记录的表的支付状态，订单信息表的订单状态
     * @param outTradeNo
     * @param resultMap
     */
    void paySuccess(String outTradeNo, Map<String, String> resultMap);
}
