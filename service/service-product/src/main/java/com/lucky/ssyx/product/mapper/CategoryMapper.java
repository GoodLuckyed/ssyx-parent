package com.lucky.ssyx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.ssyx.model.product.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品三级分类 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
