package com.lucky.ssyx.cart;

import com.lucky.ssyx.model.order.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author lucky
 * @date 2023/10/12
 */
@FeignClient(value = "service-cart")
public interface CartFeignClient {

    /**
     * 获取用户在购物车里选中的购物项列表
     * @param userId
     * @return
     */
    @GetMapping("/api/cart/inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable Long userId);
}
