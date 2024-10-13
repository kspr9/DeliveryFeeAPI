package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;

import com.fujitsu.delivery_fee_api.service.WeatherDataService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    /**
     * Manually triggers the import of weather data from the external API.
     * This endpoint can be used to force an immediate update of weather data
     * without waiting for the scheduled import.
     *
     * @return ResponseEntity with no content if the import was successful
     * @throws RuntimeException if there's an error during the import process
     */
    @PostMapping("/import")
    public ResponseEntity<Void> importWeatherData() {
        weatherDataService.importWeatherData();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Retrieves a WeatherDataDTO object by its ID.
     *
     * @param id the ID of the WeatherData object to retrieve
     * @return the WeatherDataDTO object with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeatherDataDTO> getWeatherData(@PathVariable Long id) {
        WeatherDataDTO weatherDataDTO = weatherDataService.getWeatherDataByCityId(id);
        if (weatherDataDTO != null) {
            return ResponseEntity.ok(weatherDataDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<WeatherDataDTO> getWeatherData(
        @PathVariable("city") String cityName,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        WeatherDataDTO weatherDataDTO = weatherDataService.getWeatherDataByCityName(cityName, dateTime);
        if (weatherDataDTO != null) {
            return ResponseEntity.ok(weatherDataDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Creates a new WeatherData object by saving it using the WeatherDataService.
     *
     * @param weatherDataDTO the WeatherDataDTO object to be saved
     * @return the newly created WeatherDataDTO object
     */
    @PostMapping
    public ResponseEntity<WeatherDataDTO> createWeatherData(@RequestBody WeatherDataDTO weatherDataDTO) {
        WeatherDataDTO savedWeatherDataDTO = weatherDataService.saveWeatherData(weatherDataDTO);
        return ResponseEntity.ok(savedWeatherDataDTO);
    }

    /**
     * Retrieves all WeatherData objects.
     *
     * @return a list of all WeatherDataDTO objects
     */
    @GetMapping
    public ResponseEntity<List<WeatherDataDTO>> getAllWeatherData() {
        List<WeatherDataDTO> allWeatherData = weatherDataService.getAllWeatherData();
        return ResponseEntity.ok(allWeatherData);
    }
}