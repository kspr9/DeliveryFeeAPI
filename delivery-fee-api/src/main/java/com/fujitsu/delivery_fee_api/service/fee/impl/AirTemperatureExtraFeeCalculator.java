package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.model.VehicleType;
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

    /**
     * Calculates the extra fee for a given weather data, vehicle type, and date time.
     *
     * @param weatherData    the weather data to calculate the extra fee for
     * @param vehicleType    the vehicle type to calculate the extra fee for
     * @param dateTime       the date time to calculate the extra fee for
     * @return               the calculated extra fee, or BigDecimal.ZERO if no extra fee is applicable
     */
    @Override
    public BigDecimal calculateExtraFee(WeatherDataDTO weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
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
