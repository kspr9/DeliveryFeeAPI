package com.fujitsu.delivery_fee_api.service.fee;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;

public interface ExtraFeeInterface {
    BigDecimal calculateExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime);
}
