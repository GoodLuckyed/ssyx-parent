package com.lucky.ssyx.activity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/26
 */
@FeignClient(value = "service-activity")
public interface ActivityFeginClient {
    /**
     * 根据skuId列表获取营销销信息
     * @param skuIdList
     * @return
     */
    @PostMapping("/api/activity/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);
}
