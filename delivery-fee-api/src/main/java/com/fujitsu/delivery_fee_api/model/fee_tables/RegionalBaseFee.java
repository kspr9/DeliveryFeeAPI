package com.fujitsu.delivery_fee_api.model.fee_tables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "regional_base_fee")
public class RegionalBaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @Column(name = "base_fee")
    private BigDecimal baseFee;
    
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    // Custom constructor without id
    public RegionalBaseFee(City city, VehicleType vehicleType, BigDecimal baseFee, 
                           LocalDateTime effectiveDate, Boolean isActive) {
        this.city = city;
        this.vehicleType = vehicleType;
        this.baseFee = baseFee;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }
}