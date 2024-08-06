package com.fujitsu.delivery_fee_api.exception;

public class VehicleUsageForbiddenException extends RuntimeException {
    public VehicleUsageForbiddenException(String message) {
        super(message);
    }
}