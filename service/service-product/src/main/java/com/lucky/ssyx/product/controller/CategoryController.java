package com.lucky.ssyx.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.product.service.CategoryService;
import com.lucky.ssyx.vo.product.CategoryQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品三级分类 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Api(tags = "商品分类接口")
@RestController
@RequestMapping("/admin/product/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取商品分类分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                                  @PathVariable Long page,
                              @ApiParam(name = "size", value = "每页记录数", required = true)
                                  @PathVariable Long size,
                              @ApiParam(name = "categoryQueryVo", value = "查询对象", required = false)
                              CategoryQueryVo categoryQueryVo){
        //创建分页对象,传入当前页和每页录数
        Page<Category> pageObj = new Page<>(page,size);
        //调用service层进行查询
        IPage<Category> categoryIPage = categoryService.selectPage(pageObj, categoryQueryVo);
        return Result.ok(categoryIPage);
    }

    @ApiOperation(value = "根据id获取商品分类信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    @ApiOperation(value = "新增商品分类")
    @PostMapping("/save")
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改商品分类")
    @PutMapping("/update")
    public Result updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除商品分类")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除商品分类")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        categoryService.removeByIds(idList);
        return Result.ok(null);
    }


    @ApiOperation(value = "获取全部商品分类")
    @GetMapping("/findAllList")
    public Result findAllList(){
        List<Category> categoryList = categoryService.findAllList();
        return Result.ok(categoryList);
    }
}

