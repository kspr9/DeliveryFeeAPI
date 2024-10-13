package com.fujitsu.delivery_fee_api.service.fee;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.model.VehicleType;

public interface ExtraFeeInterface {
    BigDecimal calculateExtraFee(WeatherDataDTO weatherData, VehicleType vehicleType, LocalDateTime dateTime);
}
