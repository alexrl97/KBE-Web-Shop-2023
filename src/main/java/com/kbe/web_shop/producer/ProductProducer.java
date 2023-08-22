package com.kbe.web_shop.producer;

import com.kbe.web_shop.dto.ProductDto;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {

    @Value("direct_exchange")
    private String direct_exchange;

    @Value("product_create_routing_key")
    private String product_create_routingKey;

    @Value("product_update_routing_key")
    private String product_update_routingKey;

    @Value("product_delete_routing_key")
    private String product_delete_routingKey;



    private static final Logger LOGGER = LoggerFactory.getLogger(ProductProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(ProductDto productDto) {
        String message = Helper.toJsonString(productDto);
        LOGGER.info("Sending create message -> {}", message);
        rabbitTemplate.convertAndSend(direct_exchange, product_create_routingKey, message);
    }

    public void sendUpdateMessage(ProductDto productDto) {
        String message = Helper.toJsonString(productDto);
        LOGGER.info("Sending update message -> {}", message);
        rabbitTemplate.convertAndSend(direct_exchange, product_update_routingKey, message);
    }

    public void sendDeleteMessage(String message) {
        LOGGER.info("Sending delete message -> {}", message);
        rabbitTemplate.convertAndSend(direct_exchange, product_delete_routingKey, message);
    }
}
