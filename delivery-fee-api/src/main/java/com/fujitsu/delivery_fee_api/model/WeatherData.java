package com.fujitsu.delivery_fee_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data 
@NoArgsConstructor(force = true)
@Entity 
@Table(name = "weather_data")
public class WeatherData {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String stationName;
    private Integer wmoCode;
    private Float airTemperature;
    private Float windSpeed;
    private String weatherPhenomenon;
    private Integer observationTimestamp;

    // Custom constructor without id
    public WeatherData(String stationName, Integer wmoCode, Float airTemperature, 
                       Float windSpeed, String weatherPhenomenon, Integer observationTimestamp) {
        this.stationName = stationName;
        this.wmoCode = wmoCode;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
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