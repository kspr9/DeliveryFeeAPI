package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BaseFeeDTO {
    private Long cityId;
    private Long vehicleTypeId;
    private BigDecimal baseFee;
    private LocalDateTime effectiveDate;
    private Boolean isActive;
}