package com.lucky.ssyx.payment.service;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/16
 */
public interface WeixinService {

    /**
     * 调用微信支付系统创建预付订单
     * @param orderNo
     * @return
     */
    Map<String, String> createJsapi(String orderNo);

    /**
     * 查询支付状态
     * @param orderNo
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);
}
