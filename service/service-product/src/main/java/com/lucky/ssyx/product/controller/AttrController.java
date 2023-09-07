package com.lucky.ssyx.product.controller;


import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.product.Attr;
import com.lucky.ssyx.product.service.AttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品属性 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Api(tags = "平台属性接口")
@RestController
@RequestMapping("/admin/product/attr")
@CrossOrigin
public class AttrController {

    @Autowired
    private AttrService attrService;

    @ApiOperation(value = "根据平台属性分组id获取属性列表")
    @GetMapping("/{attrGroupId}")
    public Result list(@PathVariable Long attrGroupId) {
        List<Attr> attrList = attrService.selectByAttrGroupId(attrGroupId);
        return Result.ok(attrList);
    }

    @ApiOperation(value = "根据id获取属性")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Attr attr = attrService.getById(id);
        return Result.ok(attr);
    }

    @ApiOperation(value = "新增属性")
    @PostMapping("/save")
    public Result save(@RequestBody Attr attr) {
        attrService.save(attr);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改属性")
    @PutMapping("/update")
    public Result updateById(@RequestBody Attr attr) {
        attrService.updateById(attr);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除属性")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        attrService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除属性")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrService.removeByIds(idList);
        return Result.ok(null);
    }
}

