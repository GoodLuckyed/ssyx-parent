package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lucky.ssyx.model.product.SkuPoster;
import com.lucky.ssyx.product.mapper.SkuPosterMapper;
import com.lucky.ssyx.product.service.SkuPosterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster> implements SkuPosterService {

    @Autowired
    private SkuPosterMapper skuPosterMapper;

    /**
     * 根据skuId获取图片列表
     * @param id
     * @return
     */
    @Override
    public List<SkuPoster> selectPosterList(Long id) {
        LambdaQueryWrapper<SkuPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId,id);
        List<SkuPoster> skuPosterList = skuPosterMapper.selectList(wrapper);
        return skuPosterList;
    }
}
