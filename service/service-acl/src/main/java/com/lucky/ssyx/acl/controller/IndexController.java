package com.lucky.ssyx.acl.controller;

import com.lucky.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/26
 */
@RestController
@RequestMapping("/admin/acl/index")
@Api(tags = "登录接口")
public class IndexController {

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result info(){
        Map<String,String> map = new HashMap<>();
        map.put("name","lucky");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    /**
     * 退出登录
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok(null);
    }
}
