package com.fujitsu.delivery_fee_api.model.fee_tables;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;

import jakarta.persistence.Column;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

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
    private Float baseFee;
    
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    // Constructors
    public RegionalBaseFee() {}

    public RegionalBaseFee(City city, VehicleType vehicleType, Float baseFee, LocalDateTime effectiveDate, Boolean isActive) {
        this.city = city;
        this.vehicleType = vehicleType;
        this.baseFee = baseFee;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public City getCity() {
        return city;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Float getBaseFee() {
        return baseFee;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters

    public void setCity(City city) {
        this.city = city;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setBaseFee(Float baseFee) {
        this.baseFee = baseFee;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
