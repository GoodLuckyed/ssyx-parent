package com.lucky.ssyx.home.controller;

import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.home.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/2
 */

@Api(tags = "商品详情接口")
@RestController
@RequestMapping("/api/home")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "获取sku商品详细信息")
    @GetMapping("/item/{id}")
    public Result item(@PathVariable Long id){
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> map =  itemService.item(id,userId);
        return Result.ok(map);
    }
}
