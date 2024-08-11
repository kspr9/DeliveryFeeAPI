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
        log.info("Calculating Weather Phenomenon Extra Fee for {} and {}", weatherData.getWeatherPhenomenon(), vehicleType.getVehicleType());
        
        if (!vehicleType.getExtraFeeApplicable() ) {
            log.info("No WPEF applicable for selected vehicle type");
            return BigDecimal.ZERO;
        }

        if (weatherData.getWeatherPhenomenon() == null || weatherData.getWeatherPhenomenon().trim().isEmpty()) {
            log.info("No weather phenomenon in the weather data");
            return BigDecimal.ZERO;
        }

        WeatherPhenomenonType weatherPhenomenon = getWeatherPhenomenonType(weatherData);

        return calculateFeeBasedOnPhenomenon(weatherPhenomenon, vehicleType, dateTime);
    }

    private WeatherPhenomenonType getWeatherPhenomenonType(WeatherData weatherData) {
        return weatherPhenomenonTypeRepository.findByPhenomenon(weatherData.getWeatherPhenomenon());
    }

    private BigDecimal calculateFeeBasedOnPhenomenon(WeatherPhenomenonType weatherPhenomenon, VehicleType vehicleType, LocalDateTime dateTime) {
        
        if (weatherPhenomenon.getWeatherPhenomenonCategory().equals(WeatherPhenomenonType.CATEGORY_NONE)) {
            log.info("Given Weather Phenomenon will not incur Extra Fees");
            return BigDecimal.ZERO;
        }
    
        WeatherPhenomenonExtraFee feeEntity = weatherPhenomenonExtraFeeRepository
            .findLatestByPhenomenonCategoryCodeVehicleTypeAndQueryTime(weatherPhenomenon.getWeatherPhenomenonCategory(), vehicleType.getId(), dateTime);
    
        if (feeEntity == null) {
            log.info("WPfeeEntity is null. Should it?");
            return BigDecimal.ZERO;
        }
    
        if (feeEntity.getForbidden()) {
            throw new VehicleUsageForbiddenException();
        }
    
        return feeEntity.getExtraFee();
    }
}
