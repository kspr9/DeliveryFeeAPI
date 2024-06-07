package com.fujitsu.delivery_fee_api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// import javax.persistence.ManyToOne;
// import javax.persistence.JoinColumn;
// import javax.persistence.FetchType;

@Entity
@Table(name = "weather_phenomenon_types")
public class WeatherPhenomenonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phenomenon;
    private String weatherPhenomenonCategory; // "RAIN", "SNOW OR SLEET", "THUNDER, GLAZE OR HAIL", null

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "wpef_category_id", referencedColumnName = "id")
    // private WeatherPhenomenonExtraFee wpefCategory;

    // Constructors
    public WeatherPhenomenonType() {
    }

    public WeatherPhenomenonType(String phenomenon, String weatherPhenomenonCategory) {
        this.phenomenon = phenomenon;
        this.weatherPhenomenonCategory = weatherPhenomenonCategory;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getWeatherPhenomenonCategory() {
        return weatherPhenomenonCategory;
    }

    public void setWeatherPhenomenonCategory(String weatherPhenomenonCategory) {
        // "RAIN", "SNOW OR SLEET", "THUNDER, GLAZE OR HAIL", null
        this.weatherPhenomenonCategory = weatherPhenomenonCategory;
    }
}
