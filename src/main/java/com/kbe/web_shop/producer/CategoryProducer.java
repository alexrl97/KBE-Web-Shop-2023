package com.kbe.web_shop.producer;

import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CategoryProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${category_create_routing_key}")
    private String categoryCreateRoutingKey;

    @Value("${category_update_routing_key}")
    private String categoryUpdateRoutingKey;

    @Value("${category_delete_routing_key}")
    private String categoryDeleteRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public CategoryProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(Category category) {
        String message = Helper.toJsonString(category);
        LOGGER.info("Sending create message for category -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, categoryCreateRoutingKey, message);
    }

    public void sendUpdateMessage(Category category) {
        String message = Helper.toJsonString(category);
        LOGGER.info("Sending update message for category -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, categoryUpdateRoutingKey, message);
    }

    public void sendDeleteMessage(String message) {
        LOGGER.info("Sending delete message for category -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, categoryDeleteRoutingKey, message);
    }
}
