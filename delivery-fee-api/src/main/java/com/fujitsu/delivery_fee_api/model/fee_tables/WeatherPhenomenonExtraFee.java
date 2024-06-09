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
@Table(name = "weather_phenomenon_extra_fee")
public class WeatherPhenomenonExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phenomenonCategoryCode; // "RAIN", "SNOW OR SLEET", "THUNDER, GLAZE OR HAIL", "No EF"

    @ManyToMany
    @JoinTable(
        name = "weather_phenomenon_fee_vehicle",
        joinColumns = @JoinColumn(name = "fee_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    )
    private Set<VehicleType> applicableVehicles;

    private Float extraFee;
    private Boolean forbidden;
    private LocalDateTime effectiveDate;
    private Boolean isActive;

    // Default constructor
    public WeatherPhenomenonExtraFee() {
    }

    // Parameterized constructor
    public WeatherPhenomenonExtraFee(String phenomenonCategoryCode, Set<VehicleType> applicableVehicles, Float extraFee, Boolean forbidden, LocalDateTime effectiveDate, Boolean isActive) {
        this.phenomenonCategoryCode = phenomenonCategoryCode;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getPhenomenonCategoryCode() {
        return phenomenonCategoryCode;
    }

    public void setPhenomenonCategoryCode(String phenomenonCategoryCode) {
        this.phenomenonCategoryCode = phenomenonCategoryCode;
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

    public Boolean getForbidden() {
        return forbidden;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
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
