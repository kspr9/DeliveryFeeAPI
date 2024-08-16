package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonTypeRepository;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WeatherPhenomenonExtraFeeCalculator implements ExtraFeeInterface {
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;


    public WeatherPhenomenonExtraFeeCalculator(WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository,
                                               WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository) {
        this.weatherPhenomenonTypeRepository = weatherPhenomenonTypeRepository;
        this.weatherPhenomenonExtraFeeRepository = weatherPhenomenonExtraFeeRepository;
    }

    @Override
    public BigDecimal calculateExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        String weatherPhenomenon = weatherData.getWeatherPhenomenon();
        String vehicleTypeName = vehicleType.getName();
        
        log.info("Calculating Weather Phenomenon Extra Fee for {} and {}", weatherPhenomenon, vehicleTypeName);
        
        if (!vehicleType.getExtraFeeApplicable() ) {
            log.info("No WPEF applicable for selected vehicle type");
            return BigDecimal.ZERO;
        }

        if (weatherPhenomenon == null || weatherPhenomenon.trim().isEmpty()) {
            log.info("No weather phenomenon in the weather data");
            return BigDecimal.ZERO;
        }

        return calculateFeeBasedOnPhenomenon(getWeatherPhenomenonType(weatherData), vehicleType, dateTime);
    }

    private WeatherPhenomenonType getWeatherPhenomenonType(WeatherData weatherData) {
        return weatherPhenomenonTypeRepository.findByPhenomenon(weatherData.getWeatherPhenomenon());
    }

    private BigDecimal calculateFeeBasedOnPhenomenon(WeatherPhenomenonType weatherPhenomenon, VehicleType vehicleType, LocalDateTime dateTime) {
        
        if (weatherPhenomenon.getWeatherPhenomenonCategory().equals(WeatherPhenomenonType.CATEGORY_NONE)) {
            log.info("Given Weather Phenomenon will not incur Extra Fees");
            return BigDecimal.ZERO;
        }
        
        String weatherPhenomenonCategory = weatherPhenomenon.getWeatherPhenomenonCategory();
        Long vehicleTypeId = vehicleType.getId();

        WeatherPhenomenonExtraFee feeEntity = weatherPhenomenonExtraFeeRepository
            .findLatestByPhenomenonCategoryNameVehicleTypeAndQueryTime(weatherPhenomenonCategory, vehicleTypeId, dateTime);
    
        if (feeEntity == null) {
            log.info("WPfeeEntity is null. Should it?");
            return BigDecimal.ZERO;
        }
    
        if (feeEntity.getForbidden()) {
            log.info(" Forbidden WP for selected vehicle type, {}", weatherPhenomenonCategory);
            throw new VehicleUsageForbiddenException();
        }
    
        return feeEntity.getExtraFee();
    }
}
