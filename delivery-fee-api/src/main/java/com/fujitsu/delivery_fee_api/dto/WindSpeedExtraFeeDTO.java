package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class WindSpeedExtraFeeDTO {
    private Long id;
    private Float minSpeed;
    private Float maxSpeed;
    private Set<Long> applicableVehicleIds;
    private BigDecimal extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;
}