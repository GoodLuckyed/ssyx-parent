package com.lucky.ssyx.home.controller;

import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.home.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/24
 */
@Api(tags = "小程序首页接口")
@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    @Autowired
    private HomeService homeService;

    @ApiOperation("获取首页数据")
    @GetMapping("/index")
    public Result index(){
        //获取用户id
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> resultMap = homeService.home(userId);
        return Result.ok(resultMap);
    }
}
