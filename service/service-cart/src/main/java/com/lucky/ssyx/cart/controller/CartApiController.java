package com.lucky.ssyx.cart.controller;

import com.lucky.ssyx.activity.ActivityFeginClient;
import com.lucky.ssyx.cart.service.CartInfoService;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.order.CartInfo;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lucky
 * @date 2023/10/6
 */
@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Autowired
    private CartInfoService cartInfoService;

    @Autowired
    private ActivityFeginClient activityFeginClient;

    @ApiOperation("获取购物车列表")
    @GetMapping("/cartList")
    public Result getCartList(){
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList =  cartInfoService.getCartList(userId);
        return Result.ok(cartInfoList);
    }

    @ApiOperation("获取购物车满足条件的营销与优惠券信息")
    @GetMapping("/activityCartList")
    public Result activityCartList() {
        // 获取用户id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        OrderConfirmVo orderConfirmVo = activityFeginClient.findCartActivityAndCoupon(cartInfoList, userId);
        return Result.ok(orderConfirmVo);
    }


    @ApiOperation("添加购物车")
    @GetMapping("/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId,@PathVariable("skuNum") Integer skuNum){
        //获取用户的id
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.addToCart(userId,skuId,skuNum);
        return Result.ok(null);
    }

    @ApiOperation("删除购物车的商品")
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable Long skuId){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(skuId,userId);
        return Result.ok(null);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/deleteAllCart")
    public Result deleteAllCart(){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok(null);
    }

    @ApiOperation("批量删除购物车")
    @DeleteMapping("batchDeleteCart")
    public Result batchDeleteCart(List<Long> skuIdList){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchDeleteCart(skuIdList,userId);
        return Result.ok(null);
    }
}
