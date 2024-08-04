package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import com.fujitsu.delivery_fee_api.service.DeliveryFeeCalculationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    private WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;

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

    /**
     * Calculate the delivery fee based on city, vehicle type, and optionally a specific date and time.
     *
     * @param city       The name of the city.
     * @param vehicleType The type of vehicle.
     * @param dateTime   The date and time in ISO format (optional).
     * @return The total delivery fee.
     */
    @GetMapping("/calculateDeliveryFee")
    public ResponseEntity<?> calculateDeliveryFee(
        @RequestParam String city, 
        @RequestParam String vehicleType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        
        try {
            BigDecimal totalFee = deliveryFeeService.calculateDeliveryFee(city, vehicleType, dateTime);
            return ResponseEntity.ok(totalFee);
        } catch (Exception e) {
            // Log the exception details here for debugging
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    /**
     * Add a new city to the database.
     *
     * @param  city  the city object to be saved
     * @return       the saved city object with a 201 Created status code if successful,
     *               or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the city object
     */
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        try {
            City savedCity = cityRepository.save(city);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save city", e);
        }
    }

    /**
     * Adds a new vehicle type to the database.
     *
     * @param  vehicleType   the vehicle type object to be saved
     * @return               the saved vehicle type object with a 201 Created status code if successful,
     *                       or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the vehicle type
     */
    public ResponseEntity<VehicleType> addVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        try {
            VehicleType savedVehicleType = vehicleTypeRepository.save(vehicleType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicleType);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save vehicle type", e);
        }
    }

    /**
     * Adds a new weather data entry to the database.
     *
     * @param  weatherData  the weather data object to be saved
     * @return              the saved weather data object with a 201 Created status code if successful,
     *                      or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the weather data
     */
    public ResponseEntity<WeatherData> addWeatherData(@Valid @RequestBody WeatherData weatherData) {
        try {
            WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherData);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save weather data", e);
        }
    }
    
    /**
     * Adds a new weather phenomenon type to the database.
     *
     * @param  weatherPhenomenonType   the weather phenomenon type object to be saved
     * @return                         the saved weather phenomenon type object with a 201 Created status code if successful,
     *                                 or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the weather phenomenon type
     */
    public ResponseEntity<WeatherPhenomenonType> addWeatherPhenomenonType(@Valid @RequestBody WeatherPhenomenonType weatherPhenomenonType) {
        try {
            WeatherPhenomenonType savedWeatherPhenomenonType = weatherPhenomenonTypeRepository.save(weatherPhenomenonType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherPhenomenonType);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save weather phenomenon type", e);
        }
    }

    /**
     * Adds a new regional base fee to the database.
     *
     * @param  regionalBaseFee   the regional base fee object to be saved
     * @return                   the saved regional base fee object with a 201 Created status code if successful,
     *                           or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the regional base fee
     */
    public ResponseEntity<RegionalBaseFee> addRegionalBaseFee(@Valid @RequestBody RegionalBaseFee regionalBaseFee) {
        try {
            RegionalBaseFee savedRegionalBaseFee = regionalBaseFeeRepository.save(regionalBaseFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegionalBaseFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save regional base fee", e);
        }
    }

    /**
     * Adds a new AirTemperatureExtraFee object to the database.
     *
     * @param  airTemperatureExtraFee  the AirTemperatureExtraFee object to be saved
     * @return                         the saved AirTemperatureExtraFee object with a 201 Created status code if successful,
     *                                 or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the AirTemperatureExtraFee
     */
    public ResponseEntity<AirTemperatureExtraFee> addAirTemperatureExtraFee(@Valid @RequestBody AirTemperatureExtraFee airTemperatureExtraFee) {
        try {
            AirTemperatureExtraFee savedAirTemperatureExtraFee = airTemperatureExtraFeeRepository.save(airTemperatureExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAirTemperatureExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save air temperature extra fee", e);
        }
    }
    
    /**
     * Adds a new WindSpeedExtraFee object to the database.
     *
     * @param  windSpeedExtraFee   the WindSpeedExtraFee object to be saved
     * @return                     the saved WindSpeedExtraFee object with a 201 Created status code if successful,
     *                             or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException if there was an error saving the WindSpeedExtraFee
     */
    public ResponseEntity<WindSpeedExtraFee> addWindSpeedExtraFee(@Valid @RequestBody WindSpeedExtraFee windSpeedExtraFee) {
        try {
            WindSpeedExtraFee savedWindSpeedExtraFee = windSpeedExtraFeeRepository.save(windSpeedExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWindSpeedExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save wind speed extra fee", e);
        }
    }

    /**
     * Adds a new WeatherPhenomenonExtraFee object to the database.
     *
     * @param  weatherPhenomenonExtraFee   the WeatherPhenomenonExtraFee object to be saved
     * @return                              the saved WeatherPhenomenonExtraFee object with a 201 Created status code if successful,
     *                                      or a 400 Bad Request status code with an error message if there was an error
     * @throws ResponseStatusException       if there was an error saving the WeatherPhenomenonExtraFee
     */
    public ResponseEntity<WeatherPhenomenonExtraFee> addWeatherPhenomenonExtraFee(@Valid @RequestBody WeatherPhenomenonExtraFee weatherPhenomenonExtraFee) {
        try {
            WeatherPhenomenonExtraFee savedWeatherPhenomenonExtraFee = weatherPhenomenonExtraFeeRepository.save(weatherPhenomenonExtraFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeatherPhenomenonExtraFee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to save weather phenomenon extra fee", e);
        }
    }
}
