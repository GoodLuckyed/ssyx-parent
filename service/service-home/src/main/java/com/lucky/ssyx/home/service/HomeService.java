package com.lucky.ssyx.home.service;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/24
 */
public interface HomeService {

    /**
     * 获取首页数据
     * @param userId
     * @return
     */
    Map<String, Object> home(Long userId);
}
