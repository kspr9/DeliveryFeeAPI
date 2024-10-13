package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.repository.WindSpeedExtraFeeRepository;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WindSpeedExtraFeeCalculator implements ExtraFeeInterface {
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;

    public WindSpeedExtraFeeCalculator(WindSpeedExtraFeeRepository windSpeedExtraFeeRepository) {
        this.windSpeedExtraFeeRepository = windSpeedExtraFeeRepository;
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
            return BigDecimal.ZERO;
        }

        Float windSpeed = weatherData.getWindSpeed();
        Long vehicleTypeId = vehicleType.getId();

        WindSpeedExtraFee fee = windSpeedExtraFeeRepository
            .findLatestByWindSpeedAndVehicleTypeAndQueryTime(windSpeed, vehicleTypeId, dateTime);

        if (fee == null) {
            return BigDecimal.ZERO;
        }

        if (fee.getForbidden()) {
            log.info(" Forbidden WS for selected vehicle type, {}", windSpeed);
            throw new VehicleUsageForbiddenException();
        }

        return fee.getExtraFee();
    }
}
