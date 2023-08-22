package com.kbe.web_shop.exception;

public class OrderNotFoundException extends IllegalArgumentException {
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
