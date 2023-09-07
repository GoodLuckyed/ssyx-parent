package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.model.product.SkuImage;
import com.lucky.ssyx.product.mapper.SkuImageMapper;
import com.lucky.ssyx.product.service.SkuImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lucky
 * @date 2023/9/4
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {

    @Autowired
    private SkuImageMapper skuImageMapper;

    /**
     * 根据skuId获取图片列表
     * @param id
     */
    @Override
    public List<SkuImage> selectImageList(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId,id);
        List<SkuImage> skuImageList = skuImageMapper.selectList(wrapper);
        return skuImageList;
    }
}
