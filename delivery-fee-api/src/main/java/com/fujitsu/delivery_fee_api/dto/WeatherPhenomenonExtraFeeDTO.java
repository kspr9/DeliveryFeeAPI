package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonCategory;

@Data
public class WeatherPhenomenonExtraFeeDTO {
    private Long id;
    private WeatherPhenomenonCategory phenomenonCategory;
    private Set<Long> applicableVehicleIds;
    private BigDecimal extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;
}