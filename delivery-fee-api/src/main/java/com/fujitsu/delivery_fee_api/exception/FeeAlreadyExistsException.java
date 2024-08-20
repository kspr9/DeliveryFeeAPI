package com.fujitsu.delivery_fee_api.exception;

public class FeeAlreadyExistsException extends RuntimeException{
    public FeeAlreadyExistsException(String message) {
        super(message);
    }
    
}
