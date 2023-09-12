package com.kbe.web_shop.dto.wishList;

import com.google.gson.Gson;
import com.kbe.web_shop.model.User;

import javax.validation.constraints.NotNull;

public class WishListDeleteDto {

    private @NotNull Integer wishListItemId;

    private @NotNull User user;

    public WishListDeleteDto(Integer wishListItemId, User user) {
        this.wishListItemId = wishListItemId;
        this.user = user;
    }

    public Integer getWishListItemId() {
        return wishListItemId;
    }

    public void setWishListItemId(Integer wishListItemId) {
        this.wishListItemId = wishListItemId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static WishListDeleteDto fromJsonString(String wishListDeleteDtoJsonString) {
        return new Gson().fromJson(wishListDeleteDtoJsonString, WishListDeleteDto.class);
    }
}
