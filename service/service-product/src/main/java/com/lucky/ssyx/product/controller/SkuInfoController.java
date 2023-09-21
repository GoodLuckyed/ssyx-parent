package com.lucky.ssyx.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.service.SkuInfoService;
import com.lucky.ssyx.vo.product.SkuInfoQueryVo;
import com.lucky.ssyx.vo.product.SkuInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Api(tags = "SKU管理接口")
@RestController
@RequestMapping("/admin/product/skuInfo")
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("获取SKU分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                              @PathVariable Long page,
                              @ApiParam(name = "size", value = "每页记录数", required = true)
                              @PathVariable Long size,
                              @ApiParam(name = "categoryQueryVo", value = "查询对象", required = false)
                              SkuInfoQueryVo skuInfoQueryVo){
        //创建分页对象,传入当前页和每页录数
        Page<SkuInfo> pageObj = new Page<>(page,size);
        //调用service层进行查询
        IPage<SkuInfo> skuInfoIPage = skuInfoService.selectPage(pageObj, skuInfoQueryVo);
        return Result.ok(skuInfoIPage);
    }

    @ApiOperation("新增SKU商品信息")
    @PostMapping("/save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation("根据id获取sku商品信息")
    @GetMapping("/get/{id}")
    public Result getSkuInfo(@PathVariable Long id){
        SkuInfoVo skuInfo = skuInfoService.getSkuInfo(id);
        return Result.ok(skuInfo);
    }

    @ApiOperation("修改sku商品信息")
    @PutMapping("/update")
    public Result updateSkuInfo(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除sku商品信息")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        skuInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除sku商品信息")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation("sku商品审核")
    @GetMapping("/check/{skuId}/{status}")
    public Result check(@PathVariable Long skuId, @PathVariable Integer status) {
        skuInfoService.check(skuId, status);
        return Result.ok(null);
    }

    @ApiOperation("sku商品上架")
    @GetMapping("/publish/{skuId}/{status}")
    public Result publish(@PathVariable("skuId") Long skuId,
                          @PathVariable("status") Integer status) {
        skuInfoService.publish(skuId, status);
        return Result.ok(null);
    }

    @ApiOperation("sku商品新人专享")
    @GetMapping("/isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable Long skuId,
                              @PathVariable Integer status) {
        skuInfoService.isNewPerson(skuId, status);
        return Result.ok(null);
    }
}



















