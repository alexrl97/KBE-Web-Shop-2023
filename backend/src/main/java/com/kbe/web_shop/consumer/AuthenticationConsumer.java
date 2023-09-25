package com.kbe.web_shop.consumer;

import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationConsumer {

    @Autowired
    AuthenticationService authenticationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationConsumer.class);

    @RabbitListener(queues = {"${authentication_token_create_queue}"})
    public void consumeCreateAuthenticationTokenMessage(String message) {
        LOGGER.info("Received create authentication token message -> {}", message);
        AuthenticationToken authenticationToken = AuthenticationToken.fromJsonString(message);

        try {
            authenticationService.saveConfirmationToken(authenticationToken);
        } catch (Exception e) {
            LOGGER.error("Error while processing create authentication token message:", e);
        }
    }
}
