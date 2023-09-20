package com.lucky.ssyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.ssyx.model.activity.ActivityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@Mapper
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {
    /**
     * 获取参加营销活动的商品id
     * @param skuIdList
     * @return
     */
    List<Long> selectExistSkuIdList(@Param("skuIdList") List<Long> skuIdList);
}
