package com.lucky.ssyx.search.receiver;

import com.lucky.ssyx.common.constant.MqConst;
import com.lucky.ssyx.search.service.SkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
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
 * @date 2023/9/11
 */

/**
 * 接收消息
 */
@Component
public class SkuReceiver {

    @Autowired
    private SkuService skuService;

    /**
     * 商品上架消息,同步到es
     * @param skuId
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
                    value = @Queue(name = MqConst.QUEUE_GOODS_UPPER,durable = "true"),
                    exchange = @Exchange(name = MqConst.EXCHANGE_GOODS_DIRECT,type = ExchangeTypes.DIRECT),
                    key = {MqConst.ROUTING_GOODS_UPPER}))
    public void upperSku(Long skuId, Message message, Channel channel) throws IOException {
        if (skuId != null){
            skuService.upperGoods(skuId);
        }
        //确认消息已经消费
        //第一个参数：表示收到的消息的标号
        //第二个参数：如果为true表示可以签收多个消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 商品下架消息,同步到es
     * @param skuId
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
                    value = @Queue(name = MqConst.QUEUE_GOODS_LOWER),
                    exchange = @Exchange(name = MqConst.EXCHANGE_GOODS_DIRECT,type = ExchangeTypes.DIRECT),
                    key = {MqConst.ROUTING_GOODS_LOWER}))
    public void lowerSku(Long skuId, Message message, Channel channel) throws IOException {
        if (skuId != null){
            skuService.lowerSku(skuId);
        }
        //确认消息已经消费
        //第一个参数：表示收到的消息的标号
        //第二个参数：如果为true表示可以签收多个消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}

