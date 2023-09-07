package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.SkuAttrValue;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    /**
     * 根据skuId获取平台属性列表
     * @param id
     * @return
     */
    List<SkuAttrValue> selectAttrValueList(Long id);
}
