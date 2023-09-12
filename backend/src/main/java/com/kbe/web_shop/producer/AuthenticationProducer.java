package com.kbe.web_shop.producer;

import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${authentication_token_create_routing_key}")
    private String authenticationTokenCreateRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public AuthenticationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateAuthenticationTokenMessage(AuthenticationToken authenticationToken) {
        String message = Helper.toJsonString(authenticationToken);
        LOGGER.info("Sending create authentication token message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, authenticationTokenCreateRoutingKey, message);
    }
}
