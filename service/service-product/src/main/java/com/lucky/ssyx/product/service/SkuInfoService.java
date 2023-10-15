package com.lucky.ssyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.vo.product.SkuInfoQueryVo;
import com.lucky.ssyx.vo.product.SkuInfoVo;
import com.lucky.ssyx.vo.product.SkuStockLockVo;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author lucky
 * @since 2023-09-03
 */
public interface SkuInfoService extends IService<SkuInfo> {

    /**
     * 获取SKU分页列表
     * @param pageObj
     * @param skuInfoQueryVo
     * @return
     */
    IPage<SkuInfo> selectPage(Page<SkuInfo> pageObj, SkuInfoQueryVo skuInfoQueryVo);

    /**
     * 新增SKU商品信息
     * @param skuInfoVo
     */
    void saveSkuInfo(SkuInfoVo skuInfoVo);

    /**
     * 根据id获取sku商品信息
     * @param id
     * @return
     */
    SkuInfoVo getSkuInfo(Long id);

    /**
     * 修改sku商品信息
     * @param skuInfoVo
     */
    void updateSkuInfo(SkuInfoVo skuInfoVo);

    /**
     * sku商品审核
     * @param skuId
     * @param status
     */
    void check(Long skuId, Integer status);

    /**
     * sku商品上架
     * @param skuId
     * @param status
     */
    void publish(Long skuId, Integer status);

    /**
     * sku商品新人专享
     * @param skuId
     * @param status
     */
    void isNewPerson(Long skuId, Integer status);

    /**
     * 批量获取sku商品信息
     * @param skuIdList
     */
    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    /**
     * 根据关键字获取sku商品列表
     * @param keyword
     */
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 获取新人专享商品
     * @return
     */
    List<SkuInfo> findNewPersonSkuInfoList();

    /**
     * 根据skuId获取skuInfoVo信息
     * @param skuId
     * @return
     */
    SkuInfoVo getSkuInfoVo(Long skuId);

    /**
     * 验证锁定库存
     * @param skuStockLockVoList
     * @param orderNo
     * @return
     */
    boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo);
}
