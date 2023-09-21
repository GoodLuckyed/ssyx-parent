package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.acl.Admin;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.vo.product.CategoryQueryVo;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取商品分类分页列表
     * @param pageObj
     * @param categoryQueryVo
     * @return
     */
    IPage<Category> selectPage(Page<Category> pageObj, CategoryQueryVo categoryQueryVo);

    /**
     * 获取全部商品分类
     * @return
     */
    List<Category> findAllList();

    /**
     * 批量获取分类信息
     * @param categoryIdList
     */
    List<Category> findCategoryInfoList(List<Long> categoryIdList);
}
