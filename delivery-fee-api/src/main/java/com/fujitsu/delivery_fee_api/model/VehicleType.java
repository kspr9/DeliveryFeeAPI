package com.fujitsu.delivery_fee_api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle_types")
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vehicleType;
    private Boolean extraFeeApplicable;

    // Constructors
    public VehicleType() {
        // Default constructor
    }

    public VehicleType(String vehicleType, Boolean extraFeeApplicable) {
        this.vehicleType = vehicleType;
        this.extraFeeApplicable = extraFeeApplicable;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getExtraFeeApplicable() {
        return extraFeeApplicable;
    }

    public void setExtraFeeApplicable(Boolean extraFeeApplicable) {
        this.extraFeeApplicable = extraFeeApplicable;
    }
}
