package com.kbe.web_shop.consumer;

import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserConsumer.class);

    @RabbitListener(queues = {"${user_create_queue}"})
    public void consumeCreateUserMessage(String message) {
        LOGGER.info("Received create user message -> {}", message);
        SignUpDto signUpDto = SignUpDto.fromJsonString(message);
        userService.signUp(signUpDto);
    }
}
