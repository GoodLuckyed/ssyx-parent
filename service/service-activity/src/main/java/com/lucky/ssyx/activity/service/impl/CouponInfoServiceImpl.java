package com.lucky.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.mapper.CouponInfoMapper;
import com.lucky.ssyx.activity.mapper.CouponRangeMapper;
import com.lucky.ssyx.activity.mapper.CouponUseMapper;
import com.lucky.ssyx.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.enums.CouponRangeType;
import com.lucky.ssyx.enums.CouponStatus;
import com.lucky.ssyx.model.activity.CouponInfo;
import com.lucky.ssyx.model.activity.CouponRange;
import com.lucky.ssyx.model.activity.CouponUse;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.product.Category;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.vo.activity.CouponRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    private CouponUseMapper couponUseMapper;

    /**
     * 获取优惠卷信息分页列表
     *
     * @param pageObj
     * @return
     */
    @Override
    public IPage<CouponInfo> selectPage(Page<CouponInfo> pageObj) {
        IPage<CouponInfo> couponInfoPage = couponInfoMapper.selectPage(pageObj, null);
        couponInfoPage.getRecords().stream().map(item -> {
            item.setCouponTypeString(item.getCouponType().getComment());
            if (item.getRangeType() != null) {
                item.setRangeTypeString(item.getRangeType().getComment());
            }
            return item;
        });
        return couponInfoPage;
    }

    /**
     * 根据优惠活动id查询优惠规则
     *
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
        rangeWrapper.eq(CouponRange::getCouponId, id);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(rangeWrapper);
        //获取RangeId列表，若活动范围是sku,则rangeId对应skuId; 若活动范围是分类，则rangeId对应categoryId;
        List<Long> rangeIdList = couponRangeList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rangeIdList)) {
            //判断活动范围的类型
            if (couponInfo.getRangeType() == CouponRangeType.SKU) {
                //远程调用获取sku商品列表
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                map.put("skuInfoList", skuInfoList);
            } else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                //远程调用获取分类列表
                List<Category> categoryInfoList = productFeignClient.findCategoryInfoList(rangeIdList);
                map.put("categoryList", categoryInfoList);
            }
        }
        return map;
    }

    /**
     * 添加优惠规则
     *
     * @param couponRuleVo
     */
    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        //根据优惠id删除原有的规则
        Long couponId = couponRuleVo.getCouponId();
        LambdaQueryWrapper<CouponRange> couponRangeWrapper = new LambdaQueryWrapper<>();
        couponRangeWrapper.eq(CouponRange::getCouponId, couponId);
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
     *
     * @param skuId
     * @param userId
     * @return
     */
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {
        //远程调用获取skuInfo信息,获取分类id
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //根据skuId+categoryId+userId查询
        List<CouponInfo> couponInfoList = couponInfoMapper.selectCouponInfoList(skuId, skuInfo.getCategoryId(), userId);
        return couponInfoList;
    }

    /**
     * 获取购物车可以使用的优惠卷列表
     *
     * @param cartInfoList
     * @param userId
     * @return
     */
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        //根据用户的id获取用户全部的优惠卷
        List<CouponInfo> userAllCouponInfoList = couponInfoMapper.selectCartCouponInfoList(userId);
        if (CollectionUtils.isEmpty(userAllCouponInfoList)){
            return new ArrayList<CouponInfo>();
        }
        //根据上面返回的结果，获取优惠卷id列表
        List<Long> couponIdList = userAllCouponInfoList.stream().map(CouponInfo::getId).collect(Collectors.toList());
        //查询优惠卷对应的范围
        LambdaQueryWrapper<CouponRange> rangeWrapper = new LambdaQueryWrapper<>();
        rangeWrapper.in(CouponRange::getCouponId, couponIdList);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(rangeWrapper);
        //获取优惠券id对应的满足使用范围的购物项skuId或categoryId列表
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangeList);
        //优惠后减少金额
        BigDecimal reduceAmount = new BigDecimal("0");
        //记录最优优惠券
        CouponInfo optimalCouponInfo = null;
        //遍历所有优惠卷，判断优惠卷类型
        for (CouponInfo couponInfo : userAllCouponInfoList) {
            if (couponInfo.getRangeType() == CouponRangeType.ALL){
                //通用卷,判断是否满足优惠使用门槛,计算购物车商品的总价
                BigDecimal totalAmount = this.computeTotalAmount(cartInfoList);
                if (totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }else {
                List<Long> idList = couponIdToSkuIdMap.get(couponInfo.getId());
                //当前满足使用范围的购物项信息
                List<CartInfo> currentCartInfoList = cartInfoList.stream().filter(cartInfo -> idList.contains(cartInfo.getSkuId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(currentCartInfoList)){
                    return null;
                }
                BigDecimal totalAmount = this.computeTotalAmount(currentCartInfoList);
                if (totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }
            //计算最优的优惠卷
            if (couponInfo.getIsSelect().intValue() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        if(null != optimalCouponInfo) {
            optimalCouponInfo.setIsOptimal(1);
        }
        return userAllCouponInfoList;
    }

    /**
     * 获取购物车对应的优惠卷
     * @param cartInfoList
     * @param couponId
     * @return
     */
    @Override
    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId) {
        CouponInfo couponInfo = couponInfoMapper.selectById(couponId);
        if (couponInfo == null){
            return null;
        }
        //根据优惠id查询优惠范围列表
        LambdaQueryWrapper<CouponRange> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponRange::getCouponId,couponId);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(wrapper);

        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangeList);
        List<Long> skuIdlist = couponIdToSkuIdMap.entrySet().iterator().next().getValue();
        couponInfo.setSkuIdList(skuIdlist);
        return couponInfo;
    }

    /**
     * 更新优惠券使用状态
     * @param couponId
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId) {
        LambdaQueryWrapper<CouponUse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponUse::getCouponId,couponId)
                .eq(CouponUse::getUserId,userId)
                .eq(CouponUse::getOrderId,orderId);
        CouponUse couponUse = couponUseMapper.selectOne(queryWrapper);
        couponUse.setCouponStatus(CouponStatus.USED);
        couponUseMapper.updateById(couponUse);
    }

    //获取优惠券id对应的满足使用范围的购物项skuId或categoryId列表
    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList) {
        Map<Long, List<Long>> couponIdToSkuIdMap = new HashMap<>();
        //根据优惠卷id对couponRangeList进行分组
        Map<Long, List<CouponRange>> couponIdToCouponRangeListMap = couponRangeList.stream().
                collect(Collectors.groupingBy(CouponRange::getCouponId));
        Iterator<Map.Entry<Long, List<CouponRange>>> iterator = couponIdToCouponRangeListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<CouponRange>> entry = iterator.next();
            //优惠卷id
            Long couponId = entry.getKey();
            //优惠卷范围列表
            List<CouponRange> couponRangesList = entry.getValue();
            //创建存放skuId或categoryId的Set集合
            Set<Long> idSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : couponRangesList) {
                    if (couponRange.getRangeType() == CouponRangeType.SKU && couponRange.getRangeId().intValue() == cartInfo.getSkuId().intValue()) {
                        idSet.add(cartInfo.getSkuId());
                    } else if (couponRange.getRangeType() == CouponRangeType.CATEGORY && couponRange.getRangeId().intValue() == cartInfo.getCategoryId().intValue()) {
                        idSet.add(cartInfo.getCategoryId());
                    }else {
                        //通用
                    }
                }
            }
            couponIdToSkuIdMap.put(couponId,new ArrayList<>(idSet));
        }
        return couponIdToSkuIdMap;
    }

    //计算总金额
    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }
}










