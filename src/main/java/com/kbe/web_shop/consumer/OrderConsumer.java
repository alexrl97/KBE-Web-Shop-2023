package com.kbe.web_shop.consumer;

import com.kbe.web_shop.dto.order.OrderCreateDto;
import com.kbe.web_shop.dto.order.OrderSendDto;
import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = {"${create_order_queue}"})
    public void consumeCreateOrderMessage(String message) {
        LOGGER.info("Received create order message -> {}", message);
        OrderCreateDto orderCreateDto = OrderCreateDto.fromJsonString(message);
        orderService.createOrder(orderCreateDto.getUser(), orderCreateDto.getSessionId());
    }

    @RabbitListener(queues = {"${send_order_queue}"})
    public void consumeSendOrderMessage(String message) {
        LOGGER.info("Received send order message -> {}", message);
        OrderSendDto orderSendDto = OrderSendDto.fromJsonString(message);
        orderService.sendOrder(orderSendDto.getOrderId(), orderSendDto.getTrackingNumber());
    }
}
