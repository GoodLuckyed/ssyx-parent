package com.lucky.ssyx.home.service.impl;

import com.lucky.ssyx.activity.ActivityFeginClient;
import com.lucky.ssyx.home.service.ItemService;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.search.SearchFeignClient;
import com.lucky.ssyx.vo.product.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lucky
 * @date 2023/10/2
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ActivityFeginClient activityFeginClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private SearchFeignClient searchFeignClient;

    /**
     * 获取sku商品详细信息
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> item(Long id, Long userId) {
        HashMap<String, Object> resultMap = new HashMap<>();

        CompletableFuture<SkuInfo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //远程调用获取sku信息
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(id);
            resultMap.put("skuInfoVo", skuInfoVo);
            return skuInfoVo;
        }, threadPoolExecutor);
        CompletableFuture<Void> couponCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用获取营销和优惠信息
            Map<String, Object> activityAndCouponMap = activityFeginClient.findActivityAndCoupon(id, userId);
            resultMap.putAll(activityAndCouponMap);
        }, threadPoolExecutor);
        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用增加商品的热度
            searchFeignClient.incrHotScore(id);
        }, threadPoolExecutor);
        CompletableFuture.allOf(skuInfoCompletableFuture, couponCompletableFuture, hotCompletableFuture).join();
        return resultMap;
    }
}















