package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lucky.ssyx.model.product.Attr;
import com.lucky.ssyx.product.mapper.AttrMapper;
import com.lucky.ssyx.product.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {

    @Autowired
    private AttrMapper attrMapper;

    /**
     * 根据平台属性分组id获取属性列表
     * @param attrGroupId
     * @return
     */
    @Override
    public List<Attr> selectByAttrGroupId(Long attrGroupId) {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getAttrGroupId,attrGroupId);
        List<Attr> attrList = attrMapper.selectList(wrapper);
        return attrList;
    }
}
