package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.exception.NotFoundException;
import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;
import com.fujitsu.delivery_fee_api.repository.RegionalBaseFeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BaseFeeCalculator {
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;

    public BaseFeeCalculator(RegionalBaseFeeRepository regionalBaseFeeRepository) {
        this.regionalBaseFeeRepository = regionalBaseFeeRepository;
    }

    public BigDecimal calculateBaseFee(City city, VehicleType vehicleType, LocalDateTime dateTime) {
        
        Optional<RegionalBaseFee> baseFeeModel = regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType);
        if (baseFeeModel.isEmpty()) {
            throw new NotFoundException("Base fee not found for given City and VehicleType");
        }
        BigDecimal baseFee = regionalBaseFeeRepository.fetchLatestBaseFee(city.getId(), vehicleType.getId(), dateTime);
        
        return baseFee;
    }
}