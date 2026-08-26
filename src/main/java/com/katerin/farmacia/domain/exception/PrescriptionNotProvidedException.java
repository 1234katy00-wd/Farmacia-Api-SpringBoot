package com.katerin.farmacia.domain.exception;

public class PrescriptionNotProvidedException extends RuntimeException{
    public PrescriptionNotProvidedException(String message){
        super(message);
    }
}
