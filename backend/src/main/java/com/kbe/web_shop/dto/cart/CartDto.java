package com.kbe.web_shop.dto.cart;

import com.google.gson.Gson;
import com.kbe.web_shop.model.User;

import javax.validation.constraints.NotNull;

public class CartDto {

    private Integer id;
    private @NotNull Integer productId;
    private @NotNull Integer quantity;

    private @NotNull User user;

    public CartDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static CartDto fromJsonString(String addToCartDtoJsonString) {
        return new Gson().fromJson(addToCartDtoJsonString, CartDto.class);
    }
}
