package com.fujitsu.delivery_fee_api.exception;

public class VehicleUsageForbiddenException extends RuntimeException {
    public static final String FORBIDDEN_VEHICLE = "Usage of selected vehicle type is forbidden";

    public VehicleUsageForbiddenException() {
        super(FORBIDDEN_VEHICLE);
    }
    
    public VehicleUsageForbiddenException(String message) {
        super(message);
    }
}