package com.lucky.ssyx.order.receiver;

import com.lucky.ssyx.common.constant.MqConst;
import com.lucky.ssyx.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;


/**
 * @author lucky
 * @date 2023/10/17
 */
@Component
public class OrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER_PAY, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_PAY_DIRECT),
            key = {MqConst.ROUTING_PAY_SUCCESS}))
    public void orderPay(String orderNo, Message message, Channel channel) throws IOException {
        if (!StringUtils.isEmpty(orderNo)) {
            //修改订单的支付状态
            orderInfoService.updateOrderPayStatus(orderNo);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
