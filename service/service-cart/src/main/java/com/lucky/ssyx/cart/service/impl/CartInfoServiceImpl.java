package com.lucky.ssyx.cart.service.impl;

import com.lucky.ssyx.cart.service.CartInfoService;
import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.common.exception.SsyxException;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.product.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lucky
 * @date 2023/10/6
 */
@Service
public class CartInfoServiceImpl implements CartInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 添加购物车
     *
     * @param userId
     * @param skuId
     * @param skuNum
     */
    @Override
    public void addToCart(Long userId, Long skuId, Integer skuNum) {
        //获取购物车的key
        String cartKey = this.getKey(userId);
        //根据key从redis中获取数据
        BoundHashOperations<String, String, CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = null;
        //判断购物车里是否存在该商品
        if (boundHashOperations.hasKey(skuId.toString())) {
            //购物车里已有该商品-->更新
            cartInfo = boundHashOperations.get(skuId.toString());
            int currentSkuNum = cartInfo.getSkuNum() + skuNum;
            if (currentSkuNum < 1) {
                return;
            }
            //商品数量不能大于限购个数
            if (currentSkuNum > cartInfo.getPerLimit()) {
                throw new SsyxException(ResultCodeEnum.SKU_LIMIT_ERROR);
            }
            //添加购物车商品的数量
            cartInfo.setSkuNum(currentSkuNum);
            cartInfo.setCurrentBuyNum(currentSkuNum);
            //默认添加到购物车的商品为选中状态
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            //购物车里没有该商品-->第一次添加
            skuNum = 1;
            cartInfo = new CartInfo();
            //购物车数据是从商品详情得到=>远程调用获取商品的详情
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            if (null == skuInfo) {
                throw new SsyxException(ResultCodeEnum.DATA_ERROR);
            }
            cartInfo.setUserId(skuId);
            cartInfo.setCategoryId(skuInfo.getCategoryId());
            cartInfo.setSkuType(skuInfo.getSkuType());
            cartInfo.setIsNewPerson(skuInfo.getIsNewPerson());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setPerLimit(skuInfo.getPerLimit());
            cartInfo.setImgUrl(skuInfo.getImgUrl());
            cartInfo.setIsChecked(1);
            cartInfo.setStatus(1);
            cartInfo.setWareId(skuInfo.getWareId());
            cartInfo.setCurrentBuyNum(skuNum);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        //更新redis缓存
        boundHashOperations.put(skuId.toString(),cartInfo);
        //设置过期时间
        this.setCartKeyExpire(cartKey);
    }

    /**
     * 删除购物车的商品
     * @param skuId
     * @param userId
     */
    @Override
    public void deleteCart(Long skuId, Long userId) {
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(this.getKey(userId));
        if (boundHashOperations.hasKey(skuId.toString())){
            boundHashOperations.delete(skuId.toString());
        }
    }

    /**
     * 清空购物车
     * @param userId
     */
    @Override
    public void deleteAllCart(Long userId) {
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(this.getKey(userId));
        boundHashOperations.values().forEach(cartInfo -> {
            boundHashOperations.delete(cartInfo.getSkuId().toString());
        });
    }

    /**
     * 批量删除购物车
     * @param skuIdList
     * @param userId
     */
    @Override
    public void batchDeleteCart(List<Long> skuIdList, Long userId) {
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(this.getKey(userId));
        skuIdList.forEach(skuId -> {
            boundHashOperations.delete(skuId.toString());
        });
    }

    /**
     * 获取购物车列表
     * @param userId
     * @return
     */
    @Override
    public List<CartInfo> getCartList(Long userId) {
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (userId == null){
            return cartInfoList;
        }
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(this.getKey(userId));
        cartInfoList = boundHashOperations.values();
        if (!CollectionUtils.isEmpty(cartInfoList)){
            cartInfoList.sort(new Comparator<CartInfo>() {
                @Override
                public int compare(CartInfo o1, CartInfo o2) {
                    return o1.getCreateTime().compareTo(o2.getCreateTime());
                }
            });
        }
        return cartInfoList;
    }

    /**
     * 更新购物车选中状态
     * @param userId
     * @param skuId
     * @param isChecked
     */
    @Override
    public void checkCart(Long userId, Long skuId, Integer isChecked) {
        String cartKey = this.getKey(userId);
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = boundHashOperations.get(skuId.toString());
        if (cartInfo != null){
            cartInfo.setIsChecked(isChecked);
            boundHashOperations.put(skuId.toString(),cartInfo);
            this.setCartKeyExpire(cartKey);
        }
    }

    /**
     * 更新购物车全选状态
     * @param userId
     * @param isChecked
     */
    @Override
    public void checkAllCart(Long userId, Integer isChecked) {
        String cartKey = this.getKey(userId);
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = boundHashOperations.values();
        cartInfoList.forEach(cartInfo -> {
            cartInfo.setIsChecked(isChecked);
            boundHashOperations.put(cartInfo.getSkuId().toString(),cartInfo);
        });
        this.setCartKeyExpire(cartKey);
    }

    /**
     * 批量选中购物车
     * @param userId
     * @param skuIdList
     * @param isChecked
     */
    @Override
    public void batchCheckCart(Long userId, List<Long> skuIdList, Integer isChecked) {
        String cartKey = this.getKey(userId);
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId -> {
            CartInfo cartInfo = boundHashOperations.get(skuId.toString());
            if (cartInfo != null){
                cartInfo.setIsChecked(isChecked);
                boundHashOperations.put(skuId.toString(),cartInfo);
            }
        });
        this.setCartKeyExpire(cartKey);
    }

    /**
     * 获取用户在购物车里选中的购物项列表
     * @param userId
     * @return
     */
    @Override
    public List<CartInfo> getCartCheckedList(Long userId) {
        String cartKey = this.getKey(userId);
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = boundHashOperations.values();
        List<CartInfo> cartCheckedList = cartInfoList.stream().filter((cartInfo) -> {
            return cartInfo.getIsChecked().intValue() == 1;
        }).collect(Collectors.toList());
        return cartCheckedList;
    }

    /**
     * 删除购物车里已经购买的记录
     * @param userId
     */
    @Override
    public void deleteCartBychecked(Long userId) {
        List<CartInfo> cartCheckedList = this.getCartCheckedList(userId);
        List<Long> skuIdList = cartCheckedList.stream().map(CartInfo::getSkuId).collect(Collectors.toList());
        String cartKey = this.getKey(userId);
        BoundHashOperations<String,String,CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId -> {
            boundHashOperations.delete(skuId.toString());
        });
    }

    //根据用户的id获取购物车的key
    private String getKey(Long userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }
    //设置过期时间
    private void setCartKeyExpire(String cartKey){
        redisTemplate.expire(cartKey,RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
    }
}

