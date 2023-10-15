package com.lucky.ssyx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.model.order.OrderItem;
import com.lucky.ssyx.order.mapper.OrderItemMapper;
import com.lucky.ssyx.order.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * @author lucky
 * @date 2023/10/15
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}

