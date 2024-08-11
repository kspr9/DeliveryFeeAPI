package com.fujitsu.delivery_fee_api.service.fee.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;
import com.fujitsu.delivery_fee_api.repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;
@Component
public class AirTemperatureExtraFeeCalculator implements ExtraFeeInterface {
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;

    public AirTemperatureExtraFeeCalculator(AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository) {
        this.airTemperatureExtraFeeRepository = airTemperatureExtraFeeRepository;
    }

    @Override
    public BigDecimal calculateExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        if (!vehicleType.getExtraFeeApplicable()) {
            return BigDecimal.ZERO;
        }

        AirTemperatureExtraFee fee = airTemperatureExtraFeeRepository
            .findLatestByTemperatureAndVehicleTypeAndQueryTime(weatherData.getAirTemperature(), vehicleType.getId(), dateTime);

        return fee != null ? fee.getExtraFee() : BigDecimal.ZERO;
    }
}
