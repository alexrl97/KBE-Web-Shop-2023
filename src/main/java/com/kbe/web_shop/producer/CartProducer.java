package com.kbe.web_shop.producer;

import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartDeleteItemDto;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CartProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${cart_add_routing_key}")
    private String cartAddRoutingKey;

    @Value("${cart_update_routing_key}")
    private String cartUpdateRoutingKey;

    @Value("${cart_delete_routing_key}")
    private String cartDeleteRoutingKey;

    @Value("${cart_delete_all_routing_key}")
    private String cartDeleteAllRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public CartProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAddToCartMessage(CartDto cartDto) {
        String message = Helper.toJsonString(cartDto);
        LOGGER.info("Sending add to cart message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, cartAddRoutingKey, message);
    }

    public void sendUpdateCartItemMessage(CartDto cartDto) {
        String message = Helper.toJsonString(cartDto);
        LOGGER.info("Sending update cart item message -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, cartUpdateRoutingKey, message);
    }

    public void sendDeleteCartItemMessage(CartDeleteItemDto cartDeleteItemDto) {
        String message = Helper.toJsonString(cartDeleteItemDto);
        LOGGER.info("Sending delete cart item message for cartItemId -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, cartDeleteRoutingKey, message);
    }

    public void sendDeleteAllCartItemsMessage(User user) {
        String message = Helper.toJsonString(user);
        LOGGER.info("Sending delete all cart item message for user -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, cartDeleteAllRoutingKey, message);
    }
}
