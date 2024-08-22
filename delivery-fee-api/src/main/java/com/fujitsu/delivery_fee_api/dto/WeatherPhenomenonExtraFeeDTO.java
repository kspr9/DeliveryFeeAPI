package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class WeatherPhenomenonExtraFeeDTO {
    private Long id;
    private String phenomenonCategoryName;
    private Set<Long> applicableVehicleIds;
    private BigDecimal extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;
}