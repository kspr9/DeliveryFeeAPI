package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.exception.NotFoundException;

import com.fujitsu.delivery_fee_api.model.*;

import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;
import com.fujitsu.delivery_fee_api.service.fee.impl.BaseFeeCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationService {
    private final WeatherDataRepository weatherDataRepository;
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final List<ExtraFeeInterface> extraFeeCalculators;
    private final BaseFeeCalculator baseFeeCalculator;

    private static final String TALLINN_ZONE = "Europe/Tallinn";
    

    public BigDecimal calculateDeliveryFee(String cityName, String vehicleTypeName, LocalDateTime dateTime) {
        dateTime = Optional.ofNullable(dateTime).orElseGet(() -> {
            log.info("No dateTime provided. Using current time: {}", LocalDateTime.now());
            return LocalDateTime.now();
        });
    
        log.info("Calculating delivery fee for city: {}, vehicleType: {} and dateTime: {}", cityName, vehicleTypeName, dateTime);
    
        City city = getCityByName(cityName);
        VehicleType vehicleType = getVehicleTypeByName(vehicleTypeName);
        WeatherData weatherData = getWeatherData(city, dateTime);
    
        logMainRequestParameters(city, vehicleType, weatherData);
    
        return calculateTotalFee(city, vehicleType, weatherData, dateTime);
    }

    private BigDecimal calculateTotalFee(City city, VehicleType vehicleType, WeatherData weatherData, LocalDateTime dateTime) {
        
        BigDecimal baseFee = calculateBaseFee(city, vehicleType, dateTime);
        log.info("Base Fee: {}", baseFee);
        BigDecimal totalExtraFee = calculateTotalExtraFee(weatherData, vehicleType, dateTime);
        log.info("Total Extra Fee: {}", totalExtraFee);

        BigDecimal totalFee = baseFee.add(totalExtraFee);
        log.info("Total Fee: {}", totalFee);
    
        return totalFee;
    }

    private BigDecimal calculateTotalExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        return extraFeeCalculators.stream()
            .map(calculator -> {
                BigDecimal fee = calculator.calculateExtraFee(weatherData, vehicleType, dateTime);
                log.info("{} Extra Fee: {}", calculator.getClass().getSimpleName(), fee);
                return fee;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBaseFee(City city, VehicleType vehicleType, LocalDateTime dateTime) {
        log.info("Fetching Base Fee for {} and {}", city.getName(), vehicleType.getName());
        
        return baseFeeCalculator.calculateBaseFee(city, vehicleType, dateTime);
    }

    private void logMainRequestParameters(City city, VehicleType vehicleType, WeatherData weatherData) {
        log.info("-------------------------");
        log.info("Main request parameters: ");
        log.info("City name: {}", city.getName());
        log.info("Vehicle Type: {}", vehicleType.getName());
        log.info("Weather Data: {}", weatherData);
        log.info("Observation Timestamp: {}", weatherData.getObservationTimestamp());
        log.info("Weather Phenomenon: {}", weatherData.getWeatherPhenomenon());
        log.info("Current Wind Speed: {}", weatherData.getWindSpeed());
        log.info("Current Air Temperature: {}", weatherData.getAirTemperature());
        log.info("-------------------------");
    }

    private City getCityByName(String cityName) {
        return cityRepository.findByName(cityName);
    }

    private VehicleType getVehicleTypeByName(String vehicleTypeName) {
        return vehicleTypeRepository.findByName(vehicleTypeName);
    }

    private WeatherData getWeatherData(City city, LocalDateTime dateTime) {
        Integer epochSeconds = convertToEpochSeconds(dateTime);
        WeatherData weatherData = weatherDataRepository.findLatestByWMOCodeAsOf(city.getWmoCode(), epochSeconds);
        if (weatherData == null) {
            throw new NotFoundException("Weather data not found for city: " + city.getName() + " and dateTime: " + dateTime);
        }
        return weatherData;
    }
    
    private int convertToEpochSeconds(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.of(TALLINN_ZONE);
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        return Math.toIntExact(zonedDateTime.toEpochSecond());
    }


}