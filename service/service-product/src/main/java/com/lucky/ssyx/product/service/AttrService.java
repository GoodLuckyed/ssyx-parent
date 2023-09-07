package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.Attr;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface AttrService extends IService<Attr> {

    /**
     * 根据平台属性分组id获取属性列表
     * @param attrGroupId
     * @return
     */
    List<Attr> selectByAttrGroupId(Long attrGroupId);
}
