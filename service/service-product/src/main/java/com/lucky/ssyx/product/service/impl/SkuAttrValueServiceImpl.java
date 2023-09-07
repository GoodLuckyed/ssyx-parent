package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lucky.ssyx.model.product.SkuAttrValue;
import com.lucky.ssyx.product.mapper.SkuAttrValueMapper;
import com.lucky.ssyx.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    /**
     * 根据skuId获取平台属性列表
     * @param id
     * @return
     */
    @Override
    public List<SkuAttrValue> selectAttrValueList(Long id) {
        LambdaQueryWrapper<SkuAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId,id);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(wrapper);
        return skuAttrValueList;
    }
}
