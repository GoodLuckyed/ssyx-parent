package com.lucky.ssyx.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.lucky.ssyx.activity.ActivityFeginClient;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.enums.SkuType;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.search.repository.SkuRepository;
import com.lucky.ssyx.search.service.SkuService;
import com.lucky.ssyx.vo.search.SkuEsQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ActivityFeginClient activityFeginClient;

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

    /**
     * 获取爆款商品
     * @return
     */
    @Override
    public List<SkuEs> findHotSkuList() {
        //0代表第一页
        Pageable pageable = PageRequest.of(0, 3);
        Page<SkuEs> skuEsPage = skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = skuEsPage.getContent();
        return skuEsList;
    }

    /**
     * 获取商品分类下的商品
     * @param pageable
     * @param skuEsQueryVo
     * @return
     */
    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        //设置当前登录用户的仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        //根据springData命名规则定义方法查询
        String keyword = skuEsQueryVo.getKeyword();
        //判断keyword是否为空，如果为空，根据categoryId和wareId查询
        Page<SkuEs> page = null;
        if (StringUtils.isEmpty(keyword)){
            page = skuRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(),skuEsQueryVo.getWareId(),pageable);
        }else {
            //如果不为空，根据keyword和wareId查询
            page = skuRepository.findByKeywordAndWareId(keyword,skuEsQueryVo.getWareId(),pageable);
        }
        List<SkuEs> skuEsList = page.getContent();
        if (!CollectionUtils.isEmpty(skuEsList)){
            //获取查询到的商品id
            List<Long> skuIdList = skuEsList.stream().map(item -> item.getId()).collect(Collectors.toList());
            //查询商品参与的营销活动->远程调用service-activity模块查询
            //返回的map集合key代表skuId,value代表参与的营销活动的规则列表
            Map<Long,List<String>> skuIdToRuleListMap = activityFeginClient.findActivity(skuIdList);
            if (skuIdToRuleListMap != null){
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        return page;
    }
}




















