package com.lucky.ssyx.activity.api;

import com.lucky.ssyx.activity.service.ActivityInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/26
 */
@Api(tags = "营销与优惠券接口")
@RestController
@RequestMapping("/api/activity")
@Slf4j
public class ActivityApiController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation(value = "根据skuId列表获取营销销信息")
    @PostMapping("/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList){
        Map<Long,List<String>> resultMap = activityInfoService.findActivity(skuIdList);
        return resultMap;
    }

    @ApiOperation("根据skuId获取营销与优惠券信息")
    @GetMapping("/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String,Object> findActivityAndCoupon(@PathVariable Long skuId,@PathVariable Long userId){
        Map<String,Object> map =  activityInfoService.findActivityAndCoupon(skuId,userId);
        return map;
    }
}
