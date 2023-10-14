package com.lucky.ssyx.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;

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
}
