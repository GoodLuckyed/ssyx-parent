package com.lucky.ssyx.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.lucky.ssyx.enums.SkuType;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.search.repository.SkuRepository;
import com.lucky.ssyx.search.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lucky
 * @date 2023/9/10
 */
@Service
@Slf4j
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 商品上架
     * @param skuId
     */
    @Override
    public void upperGoods(Long skuId) {
        SkuEs skuEs = new SkuEs();
        //查询sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo == null){
            return;
        }
        //查询商品分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (category != null){
            skuEs.setCategoryId(skuInfo.getCategoryId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType().equals(SkuType.COMMON.getCode())) {  //普通商品
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        }else {  //秒杀商品
            //TODO...
        }
        SkuEs save = skuRepository.save(skuEs);
        log.info("上架商品：" + JSON.toJSONString(save));
    }

    /**
     * 商品下架
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }
}




















