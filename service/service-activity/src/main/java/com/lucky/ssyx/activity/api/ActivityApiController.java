package com.lucky.ssyx.activity.api;

import com.lucky.ssyx.activity.service.ActivityInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
