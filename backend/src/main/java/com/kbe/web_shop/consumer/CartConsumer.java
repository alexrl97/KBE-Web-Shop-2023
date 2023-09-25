package com.kbe.web_shop.consumer;

import com.kbe.web_shop.dto.cart.CartDeleteItemDto;
import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartConsumer.class);

    @Autowired
    private CartService cartService;

    @RabbitListener(queues = {"${cart_add_queue}"})
    public void consumeAddToCartMessage(String message) {
        LOGGER.info("Received add to cart message -> {}", message);
        CartDto cartDto = CartDto.fromJsonString(message);

        try {
            cartService.addToCart(cartDto);
        } catch (Exception e) {
            LOGGER.error("Error while processing add to cart message:", e);
        }
    }

    @RabbitListener(queues = {"${cart_delete_queue}"})
    public void consumeDeleteCartItemMessage(String message) {
        LOGGER.info("Received delete cart item message for cartItemId -> {}", message);
        CartDeleteItemDto cartDeleteItemDto = CartDeleteItemDto.fromJsonString(message);

        try {
            cartService.deleteCartItem(cartDeleteItemDto.getCardItemID(), cartDeleteItemDto.getUser());
        } catch (Exception e) {
            LOGGER.error("Error while processing delete cart item message:", e);
        }
    }

    @RabbitListener(queues = {"${cart_delete_all_queue}"})
    public void consumeDeleteAllCartItemsMessage(String message) {
        LOGGER.info("Received delete all cart items message for user -> {}", message);
        User user = User.fromJsonString(message);

        try {
            cartService.deleteUserCartItems(user);
        } catch (Exception e) {
            LOGGER.error("Error while processing delete all cart items message:", e);
        }
    }
}
