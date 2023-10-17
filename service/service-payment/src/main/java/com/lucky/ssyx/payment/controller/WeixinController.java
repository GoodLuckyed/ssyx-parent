package com.lucky.ssyx.payment.controller;

import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.payment.service.PaymentInfoService;
import com.lucky.ssyx.payment.service.WeixinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/10/16
 */

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/payment/weixin")
@Slf4j
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @ApiOperation("调用微信支付系统创建预付订单")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@PathVariable String orderNo){
       Map<String,String> resultMap =  weixinService.createJsapi(orderNo);
       return Result.ok(resultMap);
    }

    @ApiOperation("查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo){
        //调用service查询支付结果
        Map<String,String> resultMap =  weixinService.queryPayStatus(orderNo);
        //支付失败
        if (resultMap == null){
            return Result.build(null,ResultCodeEnum.PAYMENT_FAIL);
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))){
            //支付成功,更改支付记录的表的支付状态  订单信息表的订单状态
            String outTradeNo = resultMap.get("out_trade_no");
            paymentInfoService.paySuccess(outTradeNo,resultMap);
            return Result.ok(null);
        }
        //正在支付中
        return Result.build(null,ResultCodeEnum.PAYMENT_WAIT);
    }
}
