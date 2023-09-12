package com.kbe.web_shop.producer;

import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${user_create_routing_key}")
    private String userCreateRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateUserMessage(SignUpDto signUpDto) {
        String message = Helper.toJsonString(signUpDto);
        LOGGER.info("Sending create user message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, userCreateRoutingKey, message);
    }
}
