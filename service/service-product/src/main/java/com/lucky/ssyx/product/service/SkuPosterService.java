package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.SkuPoster;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface SkuPosterService extends IService<SkuPoster> {

    /**
     * 根据skuId获取海报列表
     * @param id
     * @return
     */
    List<SkuPoster> selectPosterList(Long id);
}
