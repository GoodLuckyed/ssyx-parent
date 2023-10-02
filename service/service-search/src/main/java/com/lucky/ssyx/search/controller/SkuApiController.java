package com.lucky.ssyx.search.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.search.service.SkuService;
import com.lucky.ssyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Result upperGoods(@PathVariable Long skuId) {
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
    public List<SkuEs> findHotSkuList() {
        List<SkuEs> skuEsList = skuService.findHotSkuList();
        return skuEsList;
    }

    @ApiOperation("获取商品分类下的商品")
    @GetMapping("/{page}/{size}")
    public Result listSku(@ApiParam(name = "page", value = "当前页码", required = true)
                          @PathVariable Integer page,
                          @ApiParam(name = "size", value = "每页记录数", required = true)
                          @PathVariable Integer size,
                          @ApiParam(name = "searchParamVo", value = "查询对象", required = false)
                                  SkuEsQueryVo skuEsQueryVo) {
        //创建分页对象
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<SkuEs> pageModel =  skuService.search(pageable, skuEsQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("更新商品热度")
    @GetMapping("/inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId){
        skuService.incrHotScore(skuId);
        return true;
    }
}
