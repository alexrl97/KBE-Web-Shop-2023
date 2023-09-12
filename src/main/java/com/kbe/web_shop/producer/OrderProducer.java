package com.kbe.web_shop.producer;

import com.kbe.web_shop.dto.order.OrderCreateDto;
import com.kbe.web_shop.dto.order.OrderSendDto;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${order_create_routing_key}")
    private String createOrderRoutingKey;

    @Value("${order_send_routing_key}")
    private String sendOrderRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateOrderMessage(OrderCreateDto orderCreateDto) {
        String message = Helper.toJsonString(orderCreateDto);
        LOGGER.info("Sending create order message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, createOrderRoutingKey, message);
    }

    public void sendSendOrderMessage(OrderSendDto orderSendDto) {
        String message = Helper.toJsonString(orderSendDto);
        LOGGER.info("Sending send order message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, sendOrderRoutingKey, message);
    }
}
