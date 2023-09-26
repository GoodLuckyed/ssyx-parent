package com.lucky.ssyx.search.service;

import com.lucky.ssyx.model.search.SkuEs;

import java.util.List;

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

    /**
     * 获取爆款商品
     * @return
     */
    List<SkuEs> findHotSkuList();
}
