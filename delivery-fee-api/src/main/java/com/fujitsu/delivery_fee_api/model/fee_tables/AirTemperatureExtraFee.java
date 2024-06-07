package com.fujitsu.delivery_fee_api.model.fee_tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fujitsu.delivery_fee_api.model.VehicleType;

import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.Set;

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
        joinColumns = @JoinColumn(name = "fee_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    )
    private Set<VehicleType> applicableVehicles;

    private Float extraFee;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    
    // Default constructor
    public AirTemperatureExtraFee() {
    }

    // Parameterized constructor
    public AirTemperatureExtraFee(Float minTemp, Float maxTemp, Set<VehicleType> applicableVehicles, Float extraFee, LocalDateTime effectiveDate, Boolean isActive) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Float minTemp) {
        this.minTemp = minTemp;
    }

    public Float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Set<VehicleType> getApplicableVehicles() {
        return applicableVehicles;
    }

    public void setApplicableVehicles(Set<VehicleType> applicableVehicles) {
        this.applicableVehicles = applicableVehicles;
    }

    public Float getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(Float extraFee) {
        this.extraFee = extraFee;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
