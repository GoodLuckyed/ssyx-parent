package com.lucky.ssyx.payment.service.Impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.enums.PaymentType;
import com.lucky.ssyx.model.order.PaymentInfo;
import com.lucky.ssyx.payment.service.PaymentInfoService;
import com.lucky.ssyx.payment.service.WeixinService;
import com.lucky.ssyx.payment.utils.ConstantPropertiesUtils;
import com.lucky.ssyx.payment.utils.HttpClient;
import com.lucky.ssyx.vo.user.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lucky
 * @date 2023/10/16
 */
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 调用微信支付系统创建预付订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> createJsapi(String orderNo) {
        try {
            //根据orderNo判断支付记录表里是否有相同的记录
            PaymentInfo paymentInfo = paymentInfoService.getPaymentInfoByOrderNo(orderNo);
            if (paymentInfo == null) {
                //如果不存在相同的记录，则向表里添加记录(支付类型 ——> 微信支付)
                paymentInfo = paymentInfoService.savePaymentInfo(orderNo, PaymentType.WEIXIN);
            }

            //封装下单接口请求参数
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("body", paymentInfo.getSubject());
            paramMap.put("out_trade_no", paymentInfo.getOrderNo());
            int totalFee = paymentInfo.getTotalAmount().multiply(new BigDecimal(100)).intValue();
            paramMap.put("total_fee", String.valueOf(totalFee));
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", ConstantPropertiesUtils.NOTIFYURL);
            paramMap.put("trade_type", "JSAPI");
            UserLoginVo userLoginVo = (UserLoginVo) redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + paymentInfo.getUserId());
            if (null != userLoginVo && !StringUtils.isEmpty(userLoginVo.getOpenId())) {
                paramMap.put("openid", userLoginVo.getOpenId());
            } else {
                paramMap.put("openid", "oD7av4igt-00GI8PqsIlg5FROYnI");
            }

            //发送下单请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置参数
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();
            //返回第三方的数据
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //再次封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", ConstantPropertiesUtils.APPID);
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, ConstantPropertiesUtils.PARTNERKEY);

            //返回结果
            Map<String, String> result = new HashMap();
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询支付状态
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            //封装参数
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderNo);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();

            //获取第三方返回的数据
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
