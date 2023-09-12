package com.lucky.ssyx.product;

import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lucky
 * @date 2023/9/10
 */
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    /**
     * 获取商品分类信息
     * @param categoryId
     * @return
     */
    @GetMapping("/api/product/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId);

    /**
     * 获取商品sku信息
     * @param skuId
     * @return
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId);
}
