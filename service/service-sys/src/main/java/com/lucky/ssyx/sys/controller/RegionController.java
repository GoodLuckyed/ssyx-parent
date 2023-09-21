package com.lucky.ssyx.sys.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.sys.Region;
import com.lucky.ssyx.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lucky
 * @date 2023/8/31
 */
@Api(tags = "区域接口")
@RestController
@RequestMapping("/admin/sys/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @ApiOperation("根据关键字获取区域列表")
    @GetMapping("/findRegionByKeyword/{keyword}")
    public Result findRegionByKeyword(@PathVariable String keyword){
        List<Region> regionList = regionService.findRegionByKeyword(keyword);
        return Result.ok(regionList);
    }
}
