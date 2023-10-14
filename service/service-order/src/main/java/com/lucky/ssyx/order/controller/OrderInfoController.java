package com.lucky.ssyx.order.controller;


import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.order.service.OrderInfoService;
import com.lucky.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}

