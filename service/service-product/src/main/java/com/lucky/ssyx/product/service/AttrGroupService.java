package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.AttrGroup;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.vo.product.AttrGroupQueryVo;

import java.util.List;

/**
 * <p>
 * 属性分组 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface AttrGroupService extends IService<AttrGroup> {

    /**
     * 获取平台属性分组分页列表
     * @param pageObj
     * @param attrGroupQueryVo
     * @return
     */
    IPage<AttrGroup> selectPage(Page<AttrGroup> pageObj, AttrGroupQueryVo attrGroupQueryVo);

    /**
     * 获取全部平台属性分组
     * @return
     */
    List<AttrGroup> findAllList();
}
