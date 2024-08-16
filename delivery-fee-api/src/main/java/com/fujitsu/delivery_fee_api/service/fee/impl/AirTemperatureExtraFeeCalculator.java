package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;
import com.fujitsu.delivery_fee_api.repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AirTemperatureExtraFeeCalculator implements ExtraFeeInterface {
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;

    public AirTemperatureExtraFeeCalculator(AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository) {
        this.airTemperatureExtraFeeRepository = airTemperatureExtraFeeRepository;
    }

    @Override
    public BigDecimal calculateExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        if (!vehicleType.getExtraFeeApplicable()) {
            log.info("ATEF not applicable for selected vehicle type, {}", vehicleType.getName());
            return BigDecimal.ZERO;
        }
    
        Float airTemperature = weatherData.getAirTemperature();
        Long vehicleTypeId = vehicleType.getId();
        
        return airTemperatureExtraFeeRepository
            .findLatestByTemperatureAndVehicleTypeAndQueryTime(airTemperature, vehicleTypeId, dateTime)
            .map(AirTemperatureExtraFee::getExtraFee)
            .orElse(BigDecimal.ZERO);
    }
}
