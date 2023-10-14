package com.lucky.ssyx.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.ssyx.model.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单项信息 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2023-10-12
 */

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}
