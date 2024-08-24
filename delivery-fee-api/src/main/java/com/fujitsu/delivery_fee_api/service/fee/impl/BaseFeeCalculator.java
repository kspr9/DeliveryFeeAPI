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

    /**
     * Calculates the base fee for a given city and vehicle type at a specific date and time.
     *
     * @param  city          the city for which the base fee is to be calculated
     * @param  vehicleType   the vehicle type for which the base fee is to be calculated
     * @param  dateTime      the date and time at which the base fee is to be calculated
     * @return               the calculated base fee
     */
    public BigDecimal calculateBaseFee(City city, VehicleType vehicleType, LocalDateTime dateTime) {
        
        Long cityId = city.getId();
        Long vehicleTypeId = vehicleType.getId();
        
        Optional<RegionalBaseFee> baseFeeEntityOptional = regionalBaseFeeRepository.findLatestBaseFee(cityId, vehicleTypeId, dateTime);
        if (baseFeeEntityOptional.isEmpty()) {
            throw new NotFoundException("Base fee not found for given City and VehicleType");
        }
        
        RegionalBaseFee baseFeeEntity = baseFeeEntityOptional.get();
        return baseFeeEntity.getBaseFee();
    }
}