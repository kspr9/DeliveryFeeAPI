package com.fujitsu.delivery_fee_api.model.fee_tables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import java.time.LocalDateTime;
import java.util.Set;
import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "wind_speed_extra_fee")
public class WindSpeedExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float minSpeed;
    private Float maxSpeed;
    private BigDecimal extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
        name = "wind_speed_fee_vehicle",
        joinColumns = @JoinColumn(name = "fee_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id")
    )
    private Set<VehicleType> applicableVehicles;

    // Custom constructor without id
    public WindSpeedExtraFee(Float minSpeed, Float maxSpeed, Set<VehicleType> applicableVehicles,
                            BigDecimal extraFee, Boolean forbidden, LocalDateTime effectiveDate, 
                            Boolean isActive) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }
}