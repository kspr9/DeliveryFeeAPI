package com.fujitsu.delivery_fee_api.controller;


import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;
    private final DeliveryFeeCalculationService deliveryFeeService;


    /**
     * Calculates the delivery fee for a given city, vehicle type, and optional date/time.
     *
     * @param city          the name of the city
     * @param vehicleType   the type of vehicle
     * @param dateTime      the date and time of delivery (optional)
     */
    @GetMapping("/calculateDeliveryFee")
    public ResponseEntity<String> calculateDeliveryFee(
            @RequestParam String city,
            @RequestParam String vehicleType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        BigDecimal totalFee = deliveryFeeService.calculateDeliveryFee(city, vehicleType, dateTime);
        return ResponseEntity.ok(totalFee.toPlainString());
    }

    /**
     * Returns all existing cities
     */
    @GetMapping("/cities")
    public Iterable<City> getCities() {
        return cityRepository.findAll();
    }

    /**
     * Creates a new city.
     *
     * @param city    the city data transfer object containing the city details
     * @return        the newly created city with its ID
     */
    @PostMapping("/city")
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    /**
     * Returns all existing vehicle types
     */
    @GetMapping("/vehicleTypes")
    public Iterable<VehicleType> getVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }
    
    /**
     * Creates a new vehicle type.
     *
     * @param vehicleType   the vehicle type data transfer object containing the vehicle type details
     * @return              the newly created vehicle type with its ID
     */
    @PostMapping("/vehicleType")
    public ResponseEntity<VehicleType> addVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        VehicleType savedVehicleType = vehicleTypeRepository.save(vehicleType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicleType);
    }

    /**
     * Returns all existing weather data
     * 
     */
    @GetMapping("/weatherData")
    public Iterable<WeatherData> getWeatherData() {
        return weatherDataRepository.findAll();
    }

    /**
     * Creates a new weather data entry.
     *
     * @param weatherData	the weather data transfer object containing the weather data details
     * @return           	the newly created weather data with its ID
     */
    @PostMapping("/weatherData")
    public ResponseEntity<WeatherData> addWeatherData(@Valid @RequestBody WeatherData weatherData) {
        WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherData);
    }

    /**
     * Returns all existing weather phenomenon entries
     * 
     */
    @GetMapping("/weatherPhenomenonType")
    public Iterable<WeatherPhenomenonType> getWeatherPhenomenonType() {
        return weatherPhenomenonTypeRepository.findAll();
    }

    /**
     * Creates a new weather phenomenon type.
     *
     * @param weatherPhenomenonType	the weather phenomenon type data transfer object containing the weather phenomenon type details
     * @return         				the newly created weather phenomenon type with its ID
     */
    @PostMapping("/weatherPhenomenonType")
    public ResponseEntity<WeatherPhenomenonType> addWeatherPhenomenonType(@Valid @RequestBody WeatherPhenomenonType weatherPhenomenonType) {
        WeatherPhenomenonType savedWeatherPhenomenonType = weatherPhenomenonTypeRepository.save(weatherPhenomenonType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherPhenomenonType);
    }



}
