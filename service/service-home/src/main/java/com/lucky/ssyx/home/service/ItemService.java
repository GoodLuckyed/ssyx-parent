package com.lucky.ssyx.home.service;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/2
 */
public interface ItemService {
    /**
     * 获取sku商品详细信息
     * @param id
     * @param userId
     * @return
     */
    Map<String, Object> item(Long id, Long userId);
}
