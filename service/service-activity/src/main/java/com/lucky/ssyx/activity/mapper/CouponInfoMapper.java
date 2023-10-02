package com.lucky.ssyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.ssyx.model.activity.CouponInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@Mapper
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    /**
     *根据skuId+categoryId+userId查询优惠卷信息
     * @param skuId
     * @param categoryId
     * @param userId
     * @return
     */
    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId,@Param("categoryId") Long categoryId, @Param("userId") Long userId);
}
