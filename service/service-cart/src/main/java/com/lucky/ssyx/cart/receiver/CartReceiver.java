package com.lucky.ssyx.cart.receiver;

import com.lucky.ssyx.cart.service.CartInfoService;
import com.lucky.ssyx.common.constant.MqConst;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lucky
 * @date 2023/10/17
 */

@Component
public class CartReceiver {

    @Autowired
    private CartInfoService cartInfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_DELETE_CART,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER_DIRECT),
            key = {MqConst.ROUTING_DELETE_CART}
    ))
    public void deleteCart(Long userId, Message message, Channel channel) throws IOException {
        if (userId != null){
            cartInfoService.deleteCartBychecked(userId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}