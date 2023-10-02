package com.lucky.ssyx.search.service;

import com.lucky.ssyx.model.search.SkuEs;
import com.lucky.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/10
 */
public interface SkuService {

    /**
     * 商品上架
     * @param skuId
     */
    void upperGoods(Long skuId);

    /**
     * 商品下架
     * @param skuId
     */
    void lowerSku(Long skuId);

    /**
     * 获取爆款商品
     * @return
     */
    List<SkuEs> findHotSkuList();

    /**
     * 获取商品分类下的商品
     * @param pageable
     * @param skuEsQueryVo
     * @return
     */
    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo);

    /**
     * 更新商品热度
     * @param skuId
     */
    void incrHotScore(Long skuId);
}
