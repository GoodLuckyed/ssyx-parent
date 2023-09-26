package com.lucky.ssyx.search;

import com.lucky.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/25
 */
@FeignClient(value = "service-search")
public interface SearchFeignClient {
    /**
     * 获取爆款商品
     * @return
     */
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList();
}
