package com.kbe.web_shop.dto.cart;

import com.google.gson.Gson;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.User;

import javax.validation.constraints.NotNull;

public class CartDeleteItemDto {

    @NotNull private int cardItemID;

    @NotNull private User user;

    public int getCardItemID() {
        return cardItemID;
    }

    public void setCardItemID(int cardItemID) {
        this.cardItemID = cardItemID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CartDeleteItemDto(int cardItemID, User user) {
        this.cardItemID = cardItemID;
        this.user = user;
    }

    public static CartDeleteItemDto fromJsonString(String CartDeleteItemDtoJsonString) {
        return new Gson().fromJson(CartDeleteItemDtoJsonString, CartDeleteItemDto.class);
    }
}
