package com.fujitsu.delivery_fee_api.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/delivery")
@AllArgsConstructor
public class DeliveryFeeController {
    
    private final DeliveryFeeCalculationService deliveryFeeService;


    /**
     * Calculates the delivery fee for a given city, vehicle type, and optional date/time.
     *
     * @param city          the name of the city
     * @param vehicleType   the type of vehicle
     * @param dateTime      the date and time of delivery (optional)
     * @return ResponseEntity containing the calculated delivery fee as a BigDecimal
     * @throws com.fujitsu.delivery_fee_api.exception.NotFoundException if the city, vehicle type, or weather data is not found
     * @throws com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException if the vehicle usage is forbidden under current weather conditions
     */
    @GetMapping("/city/{city}/vehicle/{vehicleType}")
    public ResponseEntity<BigDecimal> calculateDeliveryFee(
            @PathVariable("city") String cityName,
            @PathVariable("vehicleType") String vehicleTypeName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        BigDecimal totalFee = deliveryFeeService.calculateDeliveryFee(cityName, vehicleTypeName, dateTime);
        return ResponseEntity.ok(totalFee);
    }
}
