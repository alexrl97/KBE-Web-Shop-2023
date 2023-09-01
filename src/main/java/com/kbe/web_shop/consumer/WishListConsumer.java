package com.kbe.web_shop.consumer;

import com.kbe.web_shop.dto.wishList.WishListDeleteDto;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.service.UserService;
import com.kbe.web_shop.service.WishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WishListConsumer.class);

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    @RabbitListener(queues = {"${wishlist_create_queue}"})
    public void consumeCreateMessage(String message) {
        LOGGER.info(String.format("Received create message for wishlist -> %s", message));
        WishList wishList = WishList.fromJsonString(message);
        wishListService.createWishlist(wishList);
    }

    @RabbitListener(queues = {"${wishlist_delete_queue}"})
    public void consumeDeleteMessage(String message) {
        LOGGER.info(String.format("Received delete message for wishlist -> %s", message));
        WishListDeleteDto wishListDeleteDto = WishListDeleteDto.fromJsonString(message);
        wishListService.deleteWishListItem(wishListDeleteDto.getWishListItemId(), wishListDeleteDto.getUser());
    }
}
