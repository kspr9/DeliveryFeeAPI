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
@Table(name = "weather_phenomenon_extra_fee")
public class WeatherPhenomenonExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phenomenonCategoryCode;

    @ManyToMany
    @JoinTable(
        name = "weather_phenomenon_fee_vehicle",
        joinColumns = @JoinColumn(name = "fee_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id")
    )
    private Set<VehicleType> applicableVehicles;

    private BigDecimal extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    // Custom constructor without id
    public WeatherPhenomenonExtraFee(String phenomenonCategoryCode, Set<VehicleType> applicableVehicles,
                                     BigDecimal extraFee, Boolean forbidden, LocalDateTime effectiveDate, 
                                     Boolean isActive) {
        this.phenomenonCategoryCode = phenomenonCategoryCode;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }
}