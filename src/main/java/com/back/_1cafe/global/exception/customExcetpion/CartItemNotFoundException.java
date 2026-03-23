package com.back._1cafe.global.exception.customExcetpion;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
