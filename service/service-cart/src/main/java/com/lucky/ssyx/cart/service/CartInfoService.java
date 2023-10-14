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

    /**
     * 更新购物车选中状态
     * @param userId
     * @param skuId
     * @param isChecked
     */
    void checkCart(Long userId, Long skuId, Integer isChecked);

    /**
     * 更新购物车全选状态
     * @param userId
     * @param isChecked
     */
    void checkAllCart(Long userId, Integer isChecked);

    /**
     * 批量选中购物车
     * @param userId
     * @param skuIdList
     * @param isChecked
     */
    void batchCheckCart(Long userId, List<Long> skuIdList, Integer isChecked);

    /**
     * 获取用户在购物车里选中的购物项列表
     * @param userId
     * @return
     */
    List<CartInfo> getCartCheckedList(Long userId);
}
