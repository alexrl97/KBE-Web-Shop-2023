package com.kbe.web_shop.producer;

import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {

    @Value("direct_exchange")
    private String directExchange;

    @Value("${product_create_routing_key}")
    private String productCreateRoutingKey;

    @Value("${product_update_routing_key}")
    private String productUpdateRoutingKey;

    @Value("${product_delete_routing_key}")
    private String productDeleteRoutingKey;




    private static final Logger LOGGER = LoggerFactory.getLogger(ProductProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(ProductDto productDto) {
        String message = Helper.toJsonString(productDto);
        LOGGER.info("Sending create message for product -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, productCreateRoutingKey, message);
    }

    public void sendUpdateMessage(ProductDto productDto) {
        String message = Helper.toJsonString(productDto);
        LOGGER.info("Sending update message for product -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, productUpdateRoutingKey, message);
    }

    public void sendDeleteMessage(String message) {
        LOGGER.info("Sending delete message for product -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, productDeleteRoutingKey, message);
    }
}
