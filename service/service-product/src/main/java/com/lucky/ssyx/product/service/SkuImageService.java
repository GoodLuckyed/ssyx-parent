package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.SkuImage;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/4
 */
public interface SkuImageService extends IService<SkuImage> {
    /**
     * 根据skuId获取图片列表
     * @param id
     */
    List<SkuImage> selectImageList(Long id);
}
