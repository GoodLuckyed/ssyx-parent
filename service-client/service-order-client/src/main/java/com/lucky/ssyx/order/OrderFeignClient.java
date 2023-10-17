package com.lucky.ssyx.order;

import com.lucky.ssyx.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lucky
 * @date 2023/10/16
 */

@FeignClient(value = "service-order")
public interface OrderFeignClient {

    @GetMapping("/api/order/inner/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo);
}
