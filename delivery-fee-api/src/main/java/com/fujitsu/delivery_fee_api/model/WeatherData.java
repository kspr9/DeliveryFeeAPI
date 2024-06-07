package com.fujitsu.delivery_fee_api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stationName;
    private int wmoCode;
    private Float airTemperature;
    private Float windSpeed;
    private String weatherPhenomenon;
    private int observationTimestamp;

    // Default constructor
    public WeatherData() {
    }

    // Parameterized constructor
    public WeatherData(String stationName, int wmoCode, Float airTemperature, Float windSpeed, String weatherPhenomenon, int observationTimestamp) {
        this.stationName = stationName;
        this.wmoCode = wmoCode;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.observationTimestamp = observationTimestamp;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getWmoCode() {
        return wmoCode;
    }

    public void setWmoCode(int wmoCode) {
        this.wmoCode = wmoCode;
    }

    public Float getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Float airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public int getObservationTimestamp() {
        return observationTimestamp;
    }

    public void setObservationTimestamp(int observationTimestamp) {
        this.observationTimestamp = observationTimestamp;
    }

    // Convert to Instant
    public Instant getObservationTimestampAsInstant() {
        return Instant.ofEpochSecond(observationTimestamp);
    }

    // Convert to LocalDateTime
    public LocalDateTime getObservationTimestampAsLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(observationTimestamp), ZoneOffset.UTC);
    }
}
