package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.repository.WindSpeedExtraFeeRepository;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;

@Component
public class WindSpeedExtraFeeCalculator implements ExtraFeeInterface {
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;

    public WindSpeedExtraFeeCalculator(WindSpeedExtraFeeRepository windSpeedExtraFeeRepository) {
        this.windSpeedExtraFeeRepository = windSpeedExtraFeeRepository;
    }

    @Override
    public BigDecimal calculateExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        if (!vehicleType.getExtraFeeApplicable()) {
            return BigDecimal.ZERO;
        }

        WindSpeedExtraFee fee = windSpeedExtraFeeRepository
            .findLatestByWindSpeedAndVehicleTypeAndQueryTime(weatherData.getWindSpeed(), vehicleType.getId(), dateTime);

        if (fee == null) {
            return BigDecimal.ZERO;
        }

        if (fee.getForbidden()) {
            throw new VehicleUsageForbiddenException();
        }

        return fee.getExtraFee();
    }
}
