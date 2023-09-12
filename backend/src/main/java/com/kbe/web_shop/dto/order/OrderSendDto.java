package com.kbe.web_shop.dto.order;

import com.google.gson.Gson;

import javax.validation.constraints.NotNull;

public class OrderSendDto {

    @NotNull private int orderId;
    @NotNull private String trackingNumber;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public OrderSendDto(int orderId, String trackingNumber) {
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
    }

    public static OrderSendDto fromJsonString(String orderSendDtoJsonString) {
        return new Gson().fromJson(orderSendDtoJsonString, OrderSendDto.class);
    }
}
