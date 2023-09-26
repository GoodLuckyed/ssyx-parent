package com.lucky.ssyx.home.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.product.ProductFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/26
 */
@Api(tags = "商品分类接口")
@RestController
@RequestMapping("/api/home")
public class CategoryApiController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @ApiOperation("获取商品分类")
    @GetMapping("/category")
    public Result findAllCategoryList(){
        List<Category> allCategoryList = productFeignClient.findAllCategoryList();
        return Result.ok(allCategoryList);
    }
}
