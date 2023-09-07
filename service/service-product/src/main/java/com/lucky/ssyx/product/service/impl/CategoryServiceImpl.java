package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.product.mapper.CategoryMapper;
import com.lucky.ssyx.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.vo.product.CategoryQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取商品分类分页列表
     * @param pageObj
     * @param categoryQueryVo
     * @return
     */
    @Override
    public IPage<Category> selectPage(Page<Category> pageObj, CategoryQueryVo categoryQueryVo) {
        String name = categoryQueryVo.getName();
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            wrapper.like(Category::getName,name);
        }
        IPage<Category> categoryPage = categoryMapper.selectPage(pageObj, wrapper);
        return categoryPage;
    }

    /**
     * 获取全部商品分类
     * @return
     */
    @Override
    public List<Category> findAllList() {
        //添加排序条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        List<Category> categoryList = categoryMapper.selectList(wrapper);
        return categoryList;
    }
}
