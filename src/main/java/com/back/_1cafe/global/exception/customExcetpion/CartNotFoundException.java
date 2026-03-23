package com.back._1cafe.global.exception.customExcetpion;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String message){
        super(message);
    }
}
