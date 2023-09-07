package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.model.product.AttrGroup;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.product.mapper.AttrGroupMapper;
import com.lucky.ssyx.product.service.AttrGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.vo.product.AttrGroupQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {

    @Autowired
    private AttrGroupMapper attrGroupMapper;

    /**
     * 获取平台属性分组分页列表
     * @param pageObj
     * @param attrGroupQueryVo
     * @return
     */
    @Override
    public IPage<AttrGroup> selectPage(Page<AttrGroup> pageObj, AttrGroupQueryVo attrGroupQueryVo) {
        String name = attrGroupQueryVo.getName();
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            wrapper.like(AttrGroup::getName,name);
        }
        IPage<AttrGroup> attrGroupPage = attrGroupMapper.selectPage(pageObj, wrapper);
        return attrGroupPage;
    }

    /**
     * 获取全部平台属性分组
     * @return
     */
    @Override
    public List<AttrGroup> findAllList() {
        //添加排序条件
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AttrGroup::getSort);
        List<AttrGroup> attrGroupList = attrGroupMapper.selectList(wrapper);
        return attrGroupList;
    }
}











