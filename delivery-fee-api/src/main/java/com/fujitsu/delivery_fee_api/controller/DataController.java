package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private RegionalBaseFeeRepository regionalBaseFeeRepository;

    @Autowired
    private AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;

    @Autowired
    private WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;

    @Autowired
    private WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;

    @Autowired
    private DeliveryFeeCalculationService deliveryFeeService;

    @GetMapping("/calculateDeliveryFee")
    public ResponseEntity<?> calculateDeliveryFee(@RequestParam String city, @RequestParam String vehicleType) {
        try {
            Float totalFee = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
            return ResponseEntity.ok(totalFee);
        } catch (Exception e) {
            // Log the exception details here for debugging
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/city")
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        try {
            City savedCity = cityRepository.save(city);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save city", e);
        }
    }

    @PostMapping("/vehicleType")
    public ResponseEntity<VehicleType> addVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        try {
            VehicleType savedVehicleType = vehicleTypeRepository.save(vehicleType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicleType);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save vehicle type", e);
        }
    }

    @PostMapping("/weatherData")
    public ResponseEntity<WeatherData> addWeatherData(@Valid @RequestBody WeatherData weatherData) {
        try {
            WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherData);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save weather data", e);
        }
    }

    @PostMapping("/regionalBaseFee")
    public ResponseEntity<RegionalBaseFee> addRegionalBaseFee(@Valid @RequestBody RegionalBaseFee regionalBaseFee) {
        try {
            RegionalBaseFee savedRegionalBaseFee = regionalBaseFeeRepository.save(regionalBaseFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegionalBaseFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save regional base fee", e);
        }
    }

    @PostMapping("/airTemperatureExtraFee")
    public ResponseEntity<AirTemperatureExtraFee> addAirTemperatureExtraFee(@Valid @RequestBody AirTemperatureExtraFee airTemperatureExtraFee) {
        try {
            AirTemperatureExtraFee savedAirTemperatureExtraFee = airTemperatureExtraFeeRepository.save(airTemperatureExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAirTemperatureExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save air temperature extra fee", e);
        }
    }

    @PostMapping("/windSpeedExtraFee")
    public ResponseEntity<WindSpeedExtraFee> addWindSpeedExtraFee(@Valid @RequestBody WindSpeedExtraFee windSpeedExtraFee) {
        try {
            WindSpeedExtraFee savedWindSpeedExtraFee = windSpeedExtraFeeRepository.save(windSpeedExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWindSpeedExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save wind speed extra fee", e);
        }
    }

    @PostMapping("/weatherPhenomenonExtraFee")
    public ResponseEntity<WeatherPhenomenonExtraFee> addWeatherPhenomenonExtraFee(@Valid @RequestBody WeatherPhenomenonExtraFee weatherPhenomenonExtraFee) {
        try {
            WeatherPhenomenonExtraFee savedWeatherPhenomenonExtraFee = weatherPhenomenonExtraFeeRepository.save(weatherPhenomenonExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherPhenomenonExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save weather phenomenon extra fee", e);
        }
    }
}
