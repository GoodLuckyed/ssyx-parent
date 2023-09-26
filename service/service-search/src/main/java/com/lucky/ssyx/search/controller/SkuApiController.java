package com.lucky.ssyx.search.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.search.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/10
 */
@Api(tags = "商品同步ES接口")
@RestController
@RequestMapping("/api/search/sku")
public class SkuApiController {

    @Autowired
    private SkuService skuService;

    @ApiOperation(value = "上架商品")
    @GetMapping("/inner/upperSku/{skuId}")
    public Result upperGoods(@PathVariable Long skuId){
        skuService.upperGoods(skuId);
        return Result.ok(null);
    }

    @ApiOperation(value = "下架商品")
    @GetMapping("/inner/lowerSku/{skuId}")
    public Result lowerGoods(@PathVariable Long skuId) {
        skuService.lowerSku(skuId);
        return Result.ok(null);
    }

    @ApiOperation("获取爆款商品")
    @GetMapping("/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList(){
        List<SkuEs> skuEsList = skuService.findHotSkuList();
        return skuEsList;
    }
}
