package com.kbe.web_shop.producer;
import com.kbe.web_shop.dto.wishList.WishListDeleteDto;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WishListProducer {

    @Value("direct_exchange")
    private String directExchange;

    @Value("${wishlist_create_routing_key}")
    private String wishlistCreateRoutingKey;

    @Value("${wishlist_delete_routing_key}")
    private String wishlistDeleteRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(WishListProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public WishListProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(WishList wishList) {
        String message = Helper.toJsonString(wishList);
        LOGGER.info("Sending create message for wishlist -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, wishlistCreateRoutingKey, message);
    }

    public void sendDeleteMessage(WishListDeleteDto wishListDeleteDto) {
        String message = Helper.toJsonString(wishListDeleteDto);
        LOGGER.info("Sending delete message for wishlist -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, wishlistDeleteRoutingKey, message);
    }
}
