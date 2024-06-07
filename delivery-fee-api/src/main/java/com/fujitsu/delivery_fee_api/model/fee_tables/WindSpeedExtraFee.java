package com.fujitsu.delivery_fee_api.model.fee_tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fujitsu.delivery_fee_api.model.VehicleType;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "wind_speed_extra_fee")
public class WindSpeedExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float minSpeed;
    private Float maxSpeed;
    private Float extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
        name = "wind_speed_fee_vehicle",
        joinColumns = @JoinColumn(name = "fee_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    )
    private Set<VehicleType> applicableVehicles;

    // Constructors
    public WindSpeedExtraFee() {}

    public WindSpeedExtraFee(Float minSpeed, Float maxSpeed, Float extraFee, Boolean forbidden, LocalDateTime effectiveDate, Boolean isActive, Set<VehicleType> applicableVehicles) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
        this.applicableVehicles = applicableVehicles;
    }

    // Getters
    
    public Long getId() {
        return id;
    }

    public Float getMinSpeed() {
        return minSpeed;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public Float getExtraFee() {
        return extraFee;
    }

    public Boolean getForbidden() {
        return forbidden;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Set<VehicleType> getApplicableVehicles() {
        return applicableVehicles;
    }

    // Setters

    public void setMinSpeed(Float minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setExtraFee(Float extraFee) {
        this.extraFee = extraFee;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setApplicableVehicles(Set<VehicleType> applicableVehicles) {
        this.applicableVehicles = applicableVehicles;
    }
}
