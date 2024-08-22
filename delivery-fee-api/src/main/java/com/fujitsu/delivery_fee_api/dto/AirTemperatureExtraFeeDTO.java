package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AirTemperatureExtraFeeDTO {
    private Long id;
    private Float minTemp;
    private Float maxTemp;
    private Set<Long> applicableVehicleIds;
    private BigDecimal extraFee;
    private LocalDateTime effectiveDate;
    private Boolean isActive;
}