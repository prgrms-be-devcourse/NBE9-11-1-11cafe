package com.back._1cafe.global.exception.customExcetpion;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
