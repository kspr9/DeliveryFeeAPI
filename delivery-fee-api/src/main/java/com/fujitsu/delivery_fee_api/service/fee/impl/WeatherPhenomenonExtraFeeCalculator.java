package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonCategory;
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

    /**
     * Calculates the extra fee based on weather phenomenon for a given vehicle type and date/time.
     *
     * @param weatherData    the weather data containing the weather phenomenon
     * @param vehicleType    the type of vehicle
     * @param dateTime       the date and time for which the extra fee is being calculated
     * @return               the calculated extra fee as a BigDecimal value
     */
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

    private BigDecimal calculateFeeBasedOnPhenomenon(WeatherPhenomenonType weatherPhenomenonType, VehicleType vehicleType, LocalDateTime dateTime) {
        
        WeatherPhenomenonCategory category = weatherPhenomenonType.getCategory();
        
        if (category == WeatherPhenomenonCategory.NONE) {
            log.info("Given Weather Phenomenon will not incur Extra Fees");
            return BigDecimal.ZERO;
        }
        
        Long vehicleTypeId = vehicleType.getId();

        WeatherPhenomenonExtraFee feeEntity = weatherPhenomenonExtraFeeRepository
            .findLatestByPhenomenonCategoryVehicleTypeAndQueryTime(category, vehicleTypeId, dateTime);
    
        if (feeEntity == null) {
            log.info("WPfeeEntity is null. Should it?");
            return BigDecimal.ZERO;
        }
    
        if (feeEntity.getForbidden()) {
            log.info("Forbidden WP for selected vehicle type, {}", category);
            throw new VehicleUsageForbiddenException();
        }
    
        return feeEntity.getExtraFee();
    }
}
