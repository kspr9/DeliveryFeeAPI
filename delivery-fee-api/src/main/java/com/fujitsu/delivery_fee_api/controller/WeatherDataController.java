package com.fujitsu.delivery_fee_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.service.WeatherDataCronService;

@RestController
@RequestMapping("/api/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataCronService weatherDataCronService;

    /**
     * Manually triggers the import of weather data.
     */
    @GetMapping("/import")
    public ResponseEntity<Void> importWeatherData() {
        weatherDataCronService.importWeatherData();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Retrieves a WeatherData object by its ID.
     *
     * @param id the ID of the WeatherData object to retrieve
     * @return the WeatherData object with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public WeatherData getWeatherData(@PathVariable Long id) {
        return weatherDataCronService.getWeatherData(id);
    }

    /**
     * Creates a new WeatherData object by saving it using the WeatherDataCronService.
     *
     * @param weatherData the WeatherData object to be saved
     * @return the newly created WeatherData object
     */
    @PostMapping
    public WeatherData createWeatherData(@RequestBody WeatherData weatherData) {
        return weatherDataCronService.saveWeatherData(weatherData);
    }

}