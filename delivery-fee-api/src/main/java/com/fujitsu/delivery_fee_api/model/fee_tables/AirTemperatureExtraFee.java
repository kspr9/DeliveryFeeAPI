package com.fujitsu.delivery_fee_api.model.fee_tables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import java.time.LocalDateTime;
import java.util.Set;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "air_temperature_extra_fee")
public class AirTemperatureExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float minTemp;
    private Float maxTemp;

    @ManyToMany
    @JoinTable(
        name = "air_temperature_fee_vehicle",
        joinColumns = @JoinColumn(name = "fee_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id")
    )
    private Set<VehicleType> applicableVehicles;

    private BigDecimal extraFee;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    // Custom constructor without id
    public AirTemperatureExtraFee(Float minTemp, Float maxTemp, Set<VehicleType> applicableVehicles,
                                  BigDecimal extraFee, LocalDateTime effectiveDate, Boolean isActive) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }
}