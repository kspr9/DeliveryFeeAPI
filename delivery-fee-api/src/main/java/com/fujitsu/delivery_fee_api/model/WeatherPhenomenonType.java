package com.fujitsu.delivery_fee_api.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;


/**
 * Represents a weather phenomenon entity in the database.
 * This class is used to model different weather phenomena and map them to their extra fee categories.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "weather_phenomenon_types")
public class WeatherPhenomenonType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String phenomenon;

    @NonNull
    @Enumerated(EnumType.STRING)
    private WeatherPhenomenonCategory category;

    public static WeatherPhenomenonType createNoneType(String phenomenon) {
        return new WeatherPhenomenonType(phenomenon, WeatherPhenomenonCategory.NONE);
    }

    public static final String CATEGORY_NONE = "None";
}