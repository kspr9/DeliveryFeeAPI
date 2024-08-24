package com.fujitsu.delivery_fee_api.dto;

import lombok.Data;

@Data
public class WeatherDataDTO {
    private Long id;
    private String stationName;
    private Integer wmoCode;
    private Float airTemperature;
    private Float windSpeed;
    private String weatherPhenomenon;
    private Integer observationTimestamp;
}