package com.fujitsu.delivery_fee_api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeatherPhenomenonCategory {
    NONE("None"),
    RAIN("RAIN"),
    SNOW_OR_SLEET("SNOW OR SLEET"),
    THUNDER_GLAZE_OR_HAIL("THUNDER, GLAZE OR HAIL");

    private final String categoryName;

}