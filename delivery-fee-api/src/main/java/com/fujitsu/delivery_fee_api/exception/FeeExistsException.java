package com.fujitsu.delivery_fee_api.exception;

public class FeeExistsException extends RuntimeException{
    public FeeExistsException(String message) {
        super(message);
    }
    
}
