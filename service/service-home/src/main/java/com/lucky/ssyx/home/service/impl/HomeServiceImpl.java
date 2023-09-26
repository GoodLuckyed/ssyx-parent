package com.lucky.ssyx.home.service.impl;

import com.lucky.ssyx.home.service.HomeService;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.search.SearchFeignClient;
import com.lucky.ssyx.user.UserFeginClient;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/9/24
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserFeginClient userFeginClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private SearchFeignClient searchFeignClient;

    /**
     * 获取首页接口
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> home(Long userId) {
        HashMap<String, Object> map = new HashMap<>();
        //远程调用service-user,获取提货点地址信息
        LeaderAddressVo leaderAddressVo = userFeginClient.getUserAddressByUserId(userId);
        map.put("leaderAddressVo",leaderAddressVo);
        //远程调用service-product,获取的分类信息
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        map.put("categoryList",categoryList);
        //远程调用service-product,获取新人专享商品
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        map.put("newPersonSkuInfoList",newPersonSkuInfoList);
        //远程调用service-search,获取爆款商品
        List<SkuEs> hotSkuList = searchFeignClient.findHotSkuList();
        map.put("hotSkuList",hotSkuList);
        //封装数据到map集合返回
        return map;
    }
}










