package com.fujitsu.delivery_fee_api.model.fee_tables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonCategory;

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
    
    @Enumerated(EnumType.STRING)
    private WeatherPhenomenonCategory phenomenonCategory;

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
    public WeatherPhenomenonExtraFee(WeatherPhenomenonCategory phenomenonCategory, Set<VehicleType> applicableVehicles,
                                     BigDecimal extraFee, Boolean forbidden, LocalDateTime effectiveDate, 
                                     Boolean isActive) {
        this.phenomenonCategory = phenomenonCategory;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }
}