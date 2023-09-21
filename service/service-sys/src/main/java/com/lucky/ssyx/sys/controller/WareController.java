package com.lucky.ssyx.sys.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.sys.Ware;
import com.lucky.ssyx.sys.service.WareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lucky
 * @date 2023/8/31
 */
@Api(tags = "仓库接口")
@RestController
@RequestMapping("/admin/sys/ware")
public class WareController {

    @Autowired
    private WareService wareService;

    @ApiOperation("获取所有仓库")
    @GetMapping("/findAllList")
    public Result findAllList(){
        List<Ware> wareList = wareService.list();
        return Result.ok(wareList);
    }
}
