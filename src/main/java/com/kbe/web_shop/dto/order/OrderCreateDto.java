package com.kbe.web_shop.dto.order;

import com.google.gson.Gson;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.User;

import javax.validation.constraints.NotNull;

public class OrderCreateDto {

    @NotNull private String sessionId;
    @NotNull private User user;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderCreateDto(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public static OrderCreateDto fromJsonString(String orderCreateDtoJsonString) {
        return new Gson().fromJson(orderCreateDtoJsonString, OrderCreateDto.class);
    }
}
