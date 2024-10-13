package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.exception.NotFoundException;

import com.fujitsu.delivery_fee_api.model.*;

import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.fee.ExtraFeeInterface;
import com.fujitsu.delivery_fee_api.service.fee.impl.BaseFeeCalculator;
import com.fujitsu.delivery_fee_api.util.TimeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationService {
    private final WeatherDataService weatherDataService;
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final List<ExtraFeeInterface> extraFeeCalculators;
    private final BaseFeeCalculator baseFeeCalculator;

    

    /**
     * Calculates the delivery fee for a given city, vehicle type, and optional date/time.
     *
     * @param cityName        the name of the city
     * @param vehicleTypeName the name of the vehicle type
     * @param dateTime        the date and time for which the delivery fee is calculated
     * @return                the calculated delivery fee
     */
    public BigDecimal calculateDeliveryFee(String cityName, String vehicleTypeName, LocalDateTime dateTime) {
        dateTime = TimeUtils.getCurrentDateTimeIfNull(dateTime);
    
        log.info("Calculating delivery fee for city: {}, vehicleType: {} and dateTime: {}", cityName, vehicleTypeName, dateTime);
    
        City city = getCityByName(cityName);
        VehicleType vehicleType = getVehicleTypeByName(vehicleTypeName);
        WeatherDataDTO weatherData = getWeatherData(city, dateTime);
    
        logMainRequestParameters(city, vehicleType, weatherData);
        
        BigDecimal totalFee = calculateTotalFee(city, vehicleType, weatherData, dateTime);
        
        return totalFee.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalFee(City city, VehicleType vehicleType, WeatherDataDTO weatherData, LocalDateTime dateTime) {
        
        BigDecimal baseFee = calculateBaseFee(city, vehicleType, dateTime);
        log.info("Base Fee: {}", baseFee);
        BigDecimal totalExtraFee = calculateTotalExtraFee(weatherData, vehicleType, dateTime);
        log.info("Total Extra Fee: {}", totalExtraFee);

        BigDecimal totalFee = baseFee.add(totalExtraFee);
        log.info("Total Fee: {}", totalFee);
    
        return totalFee;
    }

    private BigDecimal calculateTotalExtraFee(WeatherDataDTO weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
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

    private void logMainRequestParameters(City city, VehicleType vehicleType, WeatherDataDTO weatherData) {
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
        City city = cityRepository.findByName(cityName);
        if (city == null) {
            throw new NotFoundException("City not found: " + cityName);
        }
        return city;
    }

    private VehicleType getVehicleTypeByName(String vehicleTypeName) {
        VehicleType vehicleType = vehicleTypeRepository.findByName(vehicleTypeName);
        if (vehicleType == null) {
            throw new NotFoundException("Vehicle type not found: " + vehicleTypeName);
        }
        return vehicleType;
    }

    private WeatherDataDTO getWeatherData(City city, LocalDateTime dateTime) {

        String cityName = city.getName();

        WeatherDataDTO weatherDataDTO = weatherDataService.getWeatherDataByCityName(cityName, dateTime);
        if (weatherDataDTO == null) {
            throw new NotFoundException("Weather data not found for city: " + city.getName() + " and dateTime: " + dateTime);
        }
        return weatherDataDTO;
    }



}