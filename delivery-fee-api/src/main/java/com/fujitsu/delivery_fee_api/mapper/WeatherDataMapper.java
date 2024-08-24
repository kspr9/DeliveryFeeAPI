package com.fujitsu.delivery_fee_api.mapper;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.dto.StationDTO;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WeatherDataMapper {

    WeatherDataDTO toDto(WeatherData weatherData);

    WeatherData toEntity(WeatherDataDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "stationName")
    @Mapping(source = "wmocode", target = "wmoCode", qualifiedByName = "parseInteger")
    @Mapping(source = "airtemperature", target = "airTemperature", qualifiedByName = "parseFloat")
    @Mapping(source = "windspeed", target = "windSpeed", qualifiedByName = "parseFloat")
    @Mapping(source = "phenomenon", target = "weatherPhenomenon")
    @Mapping(target = "observationTimestamp", ignore = true)
    WeatherData stationDtoToWeatherData(StationDTO stationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "station.name", target = "stationName")
    @Mapping(source = "station.wmocode", target = "wmoCode", qualifiedByName = "parseInteger")
    @Mapping(source = "station.airtemperature", target = "airTemperature", qualifiedByName = "parseFloat")
    @Mapping(source = "station.windspeed", target = "windSpeed", qualifiedByName = "parseFloat")
    @Mapping(source = "station.phenomenon", target = "weatherPhenomenon")
    @Mapping(source = "timestamp", target = "observationTimestamp", qualifiedByName = "parseInteger")
    WeatherDataDTO stationDtoToWeatherDataDto(StationDTO station, String timestamp);

    @Named("parseInteger")
    default Integer parseInteger(String value) {
        return value != null ? Integer.parseInt(value) : null;
    }

    @Named("parseFloat")
    default Float parseFloat(String value) {
        return value != null ? Float.parseFloat(value) : null;
    }
}