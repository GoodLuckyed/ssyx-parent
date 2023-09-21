package com.lucky.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.mapper.ActivityInfoMapper;
import com.lucky.ssyx.activity.mapper.ActivityRuleMapper;
import com.lucky.ssyx.activity.mapper.ActivitySkuMapper;
import com.lucky.ssyx.activity.service.ActivityInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.model.activity.ActivityInfo;
import com.lucky.ssyx.model.activity.ActivityRule;
import com.lucky.ssyx.model.activity.ActivitySku;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.ProductFeignClient;
import com.lucky.ssyx.vo.activity.ActivityRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private ActivityRuleMapper activityRuleMapper;

    @Autowired
    private ActivitySkuMapper activitySkuMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 获取营销活动信息分页列表
     *
     * @param pageObj
     * @return
     */
    @Override
    public IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageObj) {
        LambdaQueryWrapper<ActivityInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ActivityInfo::getId);
        Page<ActivityInfo> activityInfoPage = activityInfoMapper.selectPage(pageObj, wrapper);
        activityInfoPage.getRecords().stream().forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return activityInfoPage;
    }

    /**
     * 根据营销活动id查询营销规则数据
     *
     * @param activityId
     * @return
     */
    @Override
    public Map<String, Object> findActivityRuleList(Long activityId) {
        HashMap<String, Object> map = new HashMap<>();
        //获取规则列表
        LambdaQueryWrapper<ActivityRule> ruleWrapper = new LambdaQueryWrapper<>();
        ruleWrapper.eq(ActivityRule::getActivityId, activityId);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(ruleWrapper);
        map.put("activityRuleList", activityRuleList);

        //获取使用规则的sku商品列表
        LambdaQueryWrapper<ActivitySku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ActivitySku::getActivityId, activityId);
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(skuWrapper);
        //获取activitySkuList里的skuId
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        //远程调用,通过skuIdList获取sku商品信息
        if (skuIdList.size() != 0){
            List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
            map.put("skuInfoList", skuInfoList);
        }
        return map;
    }

    /**
     * 新增营销活动规则
     *
     * @param activityRuleVo
     */
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        //删除原有的营销活动规则列表
        activityRuleMapper.delete(new LambdaQueryWrapper<ActivityRule>().eq(ActivityRule::getActivityId, activityRuleVo.getActivityId()));
        activitySkuMapper.delete(new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, activityRuleVo.getActivityId()));
        //添加规则
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        for (ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityRuleVo.getActivityId());
            activityRule.setActivityType(activityInfoMapper.selectById(activityRuleVo.getActivityId()).getActivityType());
            activityRuleMapper.insert(activityRule);
        }
        //添加规则包含的sku商品范围
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        for (ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityRuleVo.getActivityId());
            activitySkuMapper.insert(activitySku);
        }
    }

    /**
     * 根据关键字获取sku列表
     * @param keyword
     * @return
     */
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        //远程调用service-product模块获取sku商品列表
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        if (skuInfoList.size() == 0){
            return skuInfoList;
        }
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        //判断sku商品是否参加过其他的营销活动
        List<Long> existSkuIdList = activityInfoMapper.selectExistSkuIdList(skuIdList);
        ArrayList<SkuInfo> notExistSkuInfoList = new ArrayList<>();
        for (SkuInfo skuInfo : skuInfoList) {
            if (!existSkuIdList.contains(skuInfo.getId())){
                notExistSkuInfoList.add(skuInfo);
            }
        }
        return notExistSkuInfoList;
    }
}
















