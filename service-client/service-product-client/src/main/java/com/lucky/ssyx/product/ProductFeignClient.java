package com.lucky.ssyx.product;

import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.vo.product.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    /**
     * 批量获取sku商品信息
     * @param skuIdList
     * @return
     */
    @PostMapping("/api/product/inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    /**
     * 批量获取分类信息
     * @param categoryIdList
     * @return
     */
    @PostMapping("/api/product/inner/findCategoryInfoList")
    public List<Category> findCategoryInfoList(@RequestBody List<Long> categoryIdList);

    /**
     * 根据关键字获取sku商品列表
     * @param keyword
     * @return
     */
    @GetMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable String keyword);

    /**
     * 获取所有的分类
     * @return
     */
    @GetMapping("/api/product/inner/findAllCategoryList")
    public List<Category> findAllCategoryList();

    /**
     * 获取新人专享商品
     * @return
     */
    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList();

    /**
     * 根据skuId获取skuInfoVo信息
     * @param skuId
     * @return
     */
    @GetMapping("/api/product/inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable Long skuId);
}
