package com.lucky.ssyx.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.product.AttrGroup;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.product.service.AttrGroupService;
import com.lucky.ssyx.vo.product.AttrGroupQueryVo;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性分组 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Api(tags = "平台属性分组接口")
@RestController
@RequestMapping("/admin/product/attrGroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @ApiOperation(value = "获取平台属性分组分页列表")
    @GetMapping(value = "/{page}/{size}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true)
                        @PathVariable Long page,
                        @ApiParam(name = "size", value = "每页记录数", required = true)
                        @PathVariable Long size,
                        @ApiParam(name = "attrGroupQueryVo", value = "查询对象", required = false)
                                AttrGroupQueryVo attrGroupQueryVo) {
        //创建分页对象,传入当前页和每页录数
        Page<AttrGroup> pageObj = new Page<>(page,size);
        //调用service层进行查询
        IPage<AttrGroup> attrGroupIPage = attrGroupService.selectPage(pageObj, attrGroupQueryVo);
        return Result.ok(attrGroupIPage);
    }
    @ApiOperation(value = "根据id获取平台属性分组")
    @GetMapping(value = "/get/{id}")
    public Result get(@PathVariable Long id) {
        AttrGroup attrGroup = attrGroupService.getById(id);
        return Result.ok(attrGroup);
    }

    @ApiOperation(value = "新增平台属性分组")
    @PostMapping("/save")
    public Result save(@RequestBody AttrGroup attrGroup) {
        attrGroupService.save(attrGroup);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改平台属性分组")
    @PutMapping("/update")
    public Result updateById(@RequestBody AttrGroup attrGroup) {
        attrGroupService.updateById(attrGroup);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除平台属性分组")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        attrGroupService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrGroupService.removeByIds(idList);
        return Result.ok(null);
    }
    @ApiOperation(value = "获取全部平台属性分组")
    @GetMapping("/findAllList")
    public Result findAllList(){
        List<AttrGroup> attrGroupList = attrGroupService.findAllList();
        return Result.ok(attrGroupList);
    }
}

