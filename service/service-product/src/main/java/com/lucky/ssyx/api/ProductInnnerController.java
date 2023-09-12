package com.lucky.ssyx.api;

import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.service.CategoryService;
import com.lucky.ssyx.product.service.SkuInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucky
 * @date 2023/9/10
 */

/**
 * 商品上下架操作ES接口
 */
@RestController
@RequestMapping("/api/product")
public class ProductInnnerController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("根据分类id获取分类信息")
    @GetMapping("/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId){
        Category category = categoryService.getById(categoryId);
        return category;
    }

    @ApiOperation("根据skuId获取sku信息")
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo;
    }
}
