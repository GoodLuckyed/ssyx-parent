package com.lucky.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.mapper.CouponInfoMapper;
import com.lucky.ssyx.activity.mapper.CouponRangeMapper;
import com.lucky.ssyx.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.enums.CouponRangeType;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.activity.CouponRange;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.vo.activity.CouponRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private CouponRangeMapper couponRangeMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

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

    /**
     * 根据优惠活动id查询优惠规则
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findCouponRuleListById(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        //根据优惠id查询优惠的基本信息
        CouponInfo couponInfo = couponInfoMapper.selectById(id);
        //根据优惠id查询规则列表
        LambdaQueryWrapper<CouponRange> rangeWrapper = new LambdaQueryWrapper<>();
        rangeWrapper.eq(CouponRange::getCouponId,id);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(rangeWrapper);
        //获取RangeId列表，若活动范围是sku,则rangeId对应skuId; 若活动范围是分类，则rangeId对应categoryId;
        List<Long> rangeIdList = couponRangeList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rangeIdList)){
            //判断活动范围的类型
            if (couponInfo.getRangeType() == CouponRangeType.SKU){
                //远程调用获取sku商品列表
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                map.put("skuInfoList",skuInfoList);
            }else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY){
                //远程调用获取分类列表
                List<Category> categoryInfoList = productFeignClient.findCategoryInfoList(rangeIdList);
                map.put("categoryList",categoryInfoList);
            }
        }
        return map;
    }

    /**
     * 添加优惠规则
     * @param couponRuleVo
     */
    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        //根据优惠id删除原有的规则
        Long couponId = couponRuleVo.getCouponId();
        LambdaQueryWrapper<CouponRange> couponRangeWrapper = new LambdaQueryWrapper<>();
        couponRangeWrapper.eq(CouponRange::getCouponId,couponId);
        couponRangeMapper.delete(couponRangeWrapper);

        //更新优惠基本信息
        CouponInfo couponInfo = couponInfoMapper.selectById(couponId);
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());
        couponInfoMapper.updateById(couponInfo);

        //添加规则
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            couponRange.setCouponId(couponId);
            couponRangeMapper.insert(couponRange);
        }
    }

    /**
     * 根据skuId+userId获取优惠卷信息
     * @param skuId
     * @param userId
     * @return
     */
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {
        //远程调用获取skuInfo信息,获取分类id
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //根据skuId+categoryId+userId查询
        List<CouponInfo> couponInfoList = couponInfoMapper.selectCouponInfoList(skuId,skuInfo.getCategoryId(),userId);
        return couponInfoList;
    }
}










