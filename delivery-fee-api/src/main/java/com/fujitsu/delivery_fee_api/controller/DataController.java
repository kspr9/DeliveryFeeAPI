package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * Calculate the delivery fee based on city, vehicle type, and optionally a specific date and time.
     *
     * @param city       The name of the city.
     * @param vehicleType The type of vehicle.
     * @param dateTime   The date and time in ISO format (optional).
     * @return The total delivery fee.
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
     * Add a new city to the database.
     *
     * @param  city  the city object to be saved
     * @return       the saved city object with a 201 Created status code if successful,
     *               or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the city object
     */
    @PostMapping("/city")
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    /**
     * Adds a new vehicle type to the database.
     *
     * @param  vehicleType   the vehicle type object to be saved
     * @return               the saved vehicle type object with a 201 Created status code if successful,
     *                       or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the vehicle type
     */
    @PostMapping("/vehicleType")
    public ResponseEntity<VehicleType> addVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        VehicleType savedVehicleType = vehicleTypeRepository.save(vehicleType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicleType);
    }

    /**
     * Adds a new weather data entry to the database.
     *
     * @param  weatherData  the weather data object to be saved
     * @return              the saved weather data object with a 201 Created status code if successful,
     *                      or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the weather data
     */
    @PostMapping("/weatherData")
    public ResponseEntity<WeatherData> addWeatherData(@Valid @RequestBody WeatherData weatherData) {
        WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherData);
    }
    
    /**
     * Adds a new weather phenomenon type to the database.
     *
     * @param  weatherPhenomenonType   the weather phenomenon type object to be saved
     * @return                         the saved weather phenomenon type object with a 201 Created status code if successful,
     *                                 or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the weather phenomenon type
     */
    @PostMapping("/weatherPhenomenonType")
    public ResponseEntity<WeatherPhenomenonType> addWeatherPhenomenonType(@Valid @RequestBody WeatherPhenomenonType weatherPhenomenonType) {
        WeatherPhenomenonType savedWeatherPhenomenonType = weatherPhenomenonTypeRepository.save(weatherPhenomenonType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherPhenomenonType);
    }

}
