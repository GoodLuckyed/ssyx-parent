package com.lucky.ssyx.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.order.OrderInfo;
import com.lucky.ssyx.order.service.OrderInfoService;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import com.lucky.ssyx.vo.order.OrderSubmitVo;
import com.lucky.ssyx.vo.order.OrderUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-10-12
 */
@Api(tags = "订单管理接口/api/order")
@RestController
@RequestMapping("/api/order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("确认订单")
    @GetMapping("/auth/confirmOrder")
    public Result confirm(){
        OrderConfirmVo orderConfirmVo = orderInfoService.confirm();
        return Result.ok(orderConfirmVo);
    }

    @ApiOperation("生成订单")
    @PostMapping("/auth/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderSubmitVo){
        Long orderId = orderInfoService.submitOrder(orderSubmitVo);
        return Result.ok(orderId);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/auth/getOrderInfoById/{orderId}")
    public Result getOrderInfoById(@PathVariable("orderId") Long orderId){
       OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
       return Result.ok(orderInfo);
    }

    @ApiOperation("根据orderNo获取订单信息")
    @GetMapping("/inner/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo){
        OrderInfo orderInfo = orderInfoService.getOrderInfoByOrderNo(orderNo);
        return orderInfo;
    }

    @ApiOperation("查询用户不同类型的订单")
    @GetMapping("/auth/findUserOrderPage/{page}/{limit}")
    public Result findUserOrderPage(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "orderVo", value = "查询对象", required = false)
            OrderUserQueryVo orderUserQueryVo) {
        //获取用户id
        Long userId = AuthContextHolder.getUserId();
        orderUserQueryVo.setUserId(userId);
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderInfoService.findUserOrderPage(pageParam,orderUserQueryVo);
        return Result.ok(pageModel);
    }
}

