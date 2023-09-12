package com.lucky.ssyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.constant.MqConst;
import com.lucky.ssyx.common.service.RabbitService;
import com.lucky.ssyx.model.product.SkuAttrValue;
import com.lucky.ssyx.model.product.SkuImage;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.model.product.SkuPoster;
import com.lucky.ssyx.product.mapper.SkuInfoMapper;
import com.lucky.ssyx.product.service.SkuAttrValueService;
import com.lucky.ssyx.product.service.SkuImageService;
import com.lucky.ssyx.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.product.service.SkuPosterService;
import com.lucky.ssyx.vo.product.SkuInfoQueryVo;
import com.lucky.ssyx.vo.product.SkuInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
@Service
@Transactional
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuPosterService skuPosterService;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private RabbitService rabbitService;

    /**
     * 获取SKU分页列表
     *
     * @param pageObj
     * @param skuInfoQueryVo
     * @return
     */
    @Override
    public IPage<SkuInfo> selectPage(Page<SkuInfo> pageObj, SkuInfoQueryVo skuInfoQueryVo) {
        Long categoryId = skuInfoQueryVo.getCategoryId();
        String skuType = skuInfoQueryVo.getSkuType();
        String keyword = skuInfoQueryVo.getKeyword();
        //判断查询添加是否为空
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(SkuInfo::getCategoryId, categoryId);
        }
        if (!StringUtils.isEmpty(skuType)) {
            wrapper.eq(SkuInfo::getSkuType, skuType);
        }
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(SkuInfo::getSkuName, keyword);
        }
        //查询
        Page<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(pageObj, wrapper);
        return skuInfoPage;
    }

    /**
     * 新增SKU商品信息
     *
     * @param skuInfoVo
     */
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //保存SKU商品的基本信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        skuInfoMapper.insert(skuInfo);

        //保存图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        for (SkuImage skuImage : skuImagesList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImagesList);

        //保存属性值
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
        }
        skuAttrValueService.saveBatch(skuAttrValueList);

        //保存海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        for (SkuPoster skuPoster : skuPosterList) {
            skuPoster.setSkuId(skuInfo.getId());
        }
        skuPosterService.saveBatch(skuPosterList);
    }

    /**
     * 根据id获取sku商品信息
     *
     * @param id
     * @return
     */
    @Override
    public SkuInfoVo getSkuInfo(Long id) {
        //获取基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(id);
        //获取图片列表
        List<SkuImage> skuImageList = skuImageService.selectImageList(id);
        //获取海报列表
        List<SkuPoster> skuPosterList = skuPosterService.selectPosterList(id);
        //获取平台属性列表
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.selectAttrValueList(id);
        //返回封装的数据
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    /**
     * 修改sku商品信息
     *
     * @param skuInfoVo
     */
    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        //修改基本信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        Long skuId = skuInfoVo.getId();
        skuInfoMapper.updateById(skuInfo);
        //修改图片列表信息
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId, skuId);
        skuImageService.remove(wrapper);
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (skuImagesList != null) {
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(skuId);
            }
        }
        skuImageService.saveBatch(skuImagesList);

        //修改海报列表信息
        LambdaQueryWrapper<SkuPoster> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SkuPoster::getSkuId, skuId);
        skuPosterService.remove(wrapper1);
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (skuPosterList != null) {
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuId);
            }
        }
        skuPosterService.saveBatch(skuPosterList);

        //修改平台属性列表信息
        LambdaQueryWrapper<SkuAttrValue> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SkuAttrValue::getSkuId,skuId);
        skuAttrValueService.remove(wrapper2);
        //保存属性值
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if (skuAttrValueList != null){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuId);
            }
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
    }

    /**
     * sku商品审核
     * @param skuId
     * @param status
     */
    @Override
    public void check(Long skuId, Integer status) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        skuInfo.setCheckStatus(status);
        skuInfoMapper.updateById(skuInfo);
    }

    /**
     * sku商品上架
     * @param skuId
     * @param status
     */
    @Override
    public void publish(Long skuId, Integer status) {
        if (status == 1){
            SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
            skuInfo.setPublishStatus(status);
            skuInfoMapper.updateById(skuInfo);
            //调用rabbitmq发送消息,同步到es
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT,MqConst.ROUTING_GOODS_UPPER,skuId);
        }else {
            SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
            skuInfo.setPublishStatus(status);
            skuInfoMapper.updateById(skuInfo);
            //调用rabbitmq发送消息,同步到es
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT,MqConst.ROUTING_GOODS_LOWER,skuId);
        }
    }

    /**
     * sku商品新人专享
     * @param skuId
     * @param status
     */
    @Override
    public void isNewPerson(Long skuId, Integer status) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        skuInfo.setIsNewPerson(status);
        skuInfoMapper.updateById(skuInfo);
    }
}












