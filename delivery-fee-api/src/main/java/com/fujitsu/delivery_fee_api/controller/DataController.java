package com.fujitsu.delivery_fee_api.controller;


import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
import com.fujitsu.delivery_fee_api.service.DataService;
import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    private final WeatherDataRepository weatherDataRepository;
    private final DeliveryFeeCalculationService deliveryFeeService;
    private final DataService dataService;


     /**
     * Calculates the delivery fee for a given city, vehicle type, and optional date/time.
     *
     * @param city          the name of the city
     * @param vehicleType   the type of vehicle
     * @param dateTime      the date and time of delivery (optional)
     * @return ResponseEntity containing the calculated delivery fee as a String
     * @throws com.fujitsu.delivery_fee_api.exception.NotFoundException if the city, vehicle type, or weather data is not found
     * @throws com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException if the vehicle usage is forbidden under current weather conditions
     */
    @GetMapping("/calculateDeliveryFee")
    public ResponseEntity<String> calculateDeliveryFee(
            @RequestParam String city,
            @RequestParam String vehicleType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        BigDecimal totalFee = deliveryFeeService.calculateDeliveryFee(city, vehicleType, dateTime);
        return ResponseEntity.ok(totalFee.toPlainString());
    }

    @GetMapping("/weatherData")
    public Iterable<WeatherData> getWeatherData() {
        return weatherDataRepository.findAll();
    }

    // City endpoints
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(dataService.getAllCities());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        return dataService.getCityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) {
        City createdCity = dataService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Valid @RequestBody City cityDetails) {
        return dataService.updateCity(id, cityDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cities/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        return dataService.deleteCity(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // VehicleType endpoints
    @GetMapping("/vehicleTypes")
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes() {
        return ResponseEntity.ok(dataService.getAllVehicleTypes());
    }

    @GetMapping("/vehicleTypes/{id}")
    public ResponseEntity<VehicleType> getVehicleTypeById(@PathVariable Long id) {
        return dataService.getVehicleTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehicleTypes")
    public ResponseEntity<VehicleType> createVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        VehicleType createdVehicleType = dataService.createVehicleType(vehicleType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicleType);
    }

    @PutMapping("/vehicleTypes/{id}")
    public ResponseEntity<VehicleType> updateVehicleType(@PathVariable Long id, @Valid @RequestBody VehicleType vehicleTypeDetails) {
        return dataService.updateVehicleType(id, vehicleTypeDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehicleTypes/{id}")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable Long id) {
        return dataService.deleteVehicleType(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // WeatherPhenomenonType endpoints
    @GetMapping("/weatherPhenomenonTypes")
    public ResponseEntity<List<WeatherPhenomenonType>> getAllWeatherPhenomenonTypes() {
        return ResponseEntity.ok(dataService.getAllWeatherPhenomenonTypes());
    }

    @GetMapping("/weatherPhenomenonTypes/{id}")
    public ResponseEntity<WeatherPhenomenonType> getWeatherPhenomenonTypeById(@PathVariable Long id) {
        return dataService.getWeatherPhenomenonTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/weatherPhenomenonTypes")
    public ResponseEntity<WeatherPhenomenonType> createWeatherPhenomenonType(@Valid @RequestBody WeatherPhenomenonType weatherPhenomenonType) {
        WeatherPhenomenonType createdWeatherPhenomenonType = dataService.createWeatherPhenomenonType(weatherPhenomenonType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWeatherPhenomenonType);
    }

    @PutMapping("/weatherPhenomenonTypes/{id}")
    public ResponseEntity<WeatherPhenomenonType> updateWeatherPhenomenonType(@PathVariable Long id, @Valid @RequestBody WeatherPhenomenonType weatherPhenomenonTypeDetails) {
        return dataService.updateWeatherPhenomenonType(id, weatherPhenomenonTypeDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/weatherPhenomenonTypes/{id}")
    public ResponseEntity<Void> deleteWeatherPhenomenonType(@PathVariable Long id) {
        return dataService.deleteWeatherPhenomenonType(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    



}
