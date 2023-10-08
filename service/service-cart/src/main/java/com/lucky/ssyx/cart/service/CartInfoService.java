package com.lucky.ssyx.cart.service;

import com.lucky.ssyx.model.order.CartInfo;

import java.util.List;

/**
 * @author lucky
 * @date 2023/10/6
 */
public interface CartInfoService {
    /**
     * 添加购物车
     * @param userId
     * @param skuId
     * @param skuNum
     */
    void addToCart(Long userId, Long skuId, Integer skuNum);

    /**
     * 删除购物车的商品
     * @param skuId
     * @param userId
     */
    void deleteCart(Long skuId, Long userId);

    /**
     * 清空购物车
     * @param userId
     */
    void deleteAllCart(Long userId);

    /**
     * 批量删除购物车
     * @param skuIdList
     * @param userId
     */
    void batchDeleteCart(List<Long> skuIdList, Long userId);

    /**
     * 获取购物车列表
     * @param userId
     * @return
     */
    List<CartInfo> getCartList(Long userId);
}
