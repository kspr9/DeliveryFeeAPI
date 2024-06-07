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
    private LocalDateTime effective_date;
    private Boolean is_active;

    // Default constructor
    public WeatherPhenomenonExtraFee() {
    }

    // Parameterized constructor
    public WeatherPhenomenonExtraFee(String phenomenonCategoryCode, Set<VehicleType> applicableVehicles, Float extraFee, Boolean forbidden, LocalDateTime effective_date, Boolean is_active) {
        this.phenomenonCategoryCode = phenomenonCategoryCode;
        this.applicableVehicles = applicableVehicles;
        this.extraFee = extraFee;
        this.forbidden = forbidden;
        this.effective_date = effective_date;
        this.is_active = is_active;
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
        return effective_date;
    }

    public void setEffectiveDate(LocalDateTime effective_date) {
        this.effective_date = effective_date;
    }

    public Boolean getIsActive() {
        return is_active;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }
}
