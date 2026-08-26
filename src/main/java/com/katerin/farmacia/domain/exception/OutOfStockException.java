package com.katerin.farmacia.domain.exception;

public class OutOfStockException  extends RuntimeException{
    public OutOfStockException(String message){
        super(message);
    }
    
}
