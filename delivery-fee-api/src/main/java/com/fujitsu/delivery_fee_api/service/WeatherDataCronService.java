package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
import com.fujitsu.delivery_fee_api.util.WeatherDataParser;
import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.WeatherData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataCronService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;
    
    private final RestTemplate restTemplate;
    private final WeatherDataRepository weatherDataRepository;
    private final CityRepository cityRepository;
    private final WeatherDataParser weatherDataParser;

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() {
        String xmlData = fetchWeatherData();
        
        List<City> cities = cityRepository.findAll();
        List<Integer> relevantWmoCodes = cities.stream().map(City::getWmoCode).collect(Collectors.toList());
        List<WeatherData> weatherDataList = weatherDataParser.parseWeatherData(xmlData, relevantWmoCodes);

        saveWeatherData(weatherDataList);
    }
    
    private String fetchWeatherData() {
        ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Received data successfully");
            return response.getBody();
        } else {
            log.error("Failed to fetch data: HTTP Status {}", response.getStatusCode());
            throw new RuntimeException("Failed to fetch weather data");
        }
    }

    private void saveWeatherData(List<WeatherData> weatherDataList) {
        for (WeatherData weatherData : weatherDataList) {
            log.info("Saving data: {}", weatherData.getStationName());
            weatherDataRepository.save(weatherData);
        }
        log.info("Data saved successfully");
    }

    public WeatherData getWeatherData(Long id) {
        Optional<WeatherData> weatherData = weatherDataRepository.findById(id);
        return weatherData.orElse(null);
    }
    
    public WeatherData saveWeatherData(WeatherData weatherData) {
        return weatherDataRepository.save(weatherData);
    }

}
