package com.lucky.ssyx.api;

import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.service.CategoryService;
import com.lucky.ssyx.product.service.SkuInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("批量获取sku商品信息")
    @PostMapping("/inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList){
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoList(skuIdList);
        return skuInfoList;
    }

    @ApiOperation("批量获取分类信息")
    @PostMapping("/inner/findCategoryInfoList")
    public List<Category> findCategoryInfoList(@RequestBody List<Long> categoryIdList){
        List<Category> categoryInfoList = categoryService.findCategoryInfoList(categoryIdList);
        return categoryInfoList;
    }

    @ApiOperation("根据关键字获取sku商品列表")
    @GetMapping("/inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable String keyword){
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoByKeyword(keyword);
        return skuInfoList;
    }

    @ApiOperation("获取所有的分类")
    @GetMapping("/inner/findAllCategoryList")
    public List<Category> findAllCategoryList(){
        List<Category> categoryList = categoryService.list();
        return categoryList;
    }

    @ApiOperation("获取新人专享商品")
    @GetMapping("/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList(){
       List<SkuInfo> skuInfoList =  skuInfoService.findNewPersonSkuInfoList();
       return skuInfoList;
    }
}
