package com.lucky.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.mapper.CouponInfoMapper;
import com.lucky.ssyx.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.model.activity.CouponInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    /**
     * 获取优惠卷信息分页列表
     * @param pageObj
     * @return
     */
    @Override
    public IPage<CouponInfo> selectPage(Page<CouponInfo> pageObj) {
        IPage<CouponInfo> couponInfoPage = couponInfoMapper.selectPage(pageObj, null);
        couponInfoPage.getRecords().stream().map(item -> {
            item.setCouponTypeString(item.getCouponType().getComment());
            if (item.getRangeType() != null){
                item.setRangeTypeString(item.getRangeType().getComment());
            }
            return item;
        });
        return couponInfoPage;
    }
}










