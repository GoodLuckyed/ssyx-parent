package com.lucky.ssyx.search.service;

/**
 * @author lucky
 * @date 2023/9/10
 */
public interface SkuService {

    /**
     * 商品上架
     * @param skuId
     */
    void upperGoods(Long skuId);

    /**
     * 商品下架
     * @param skuId
     */
    void lowerSku(Long skuId);
}
