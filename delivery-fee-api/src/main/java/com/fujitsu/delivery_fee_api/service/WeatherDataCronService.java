package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.dto.ObservationsDTO;
import com.fujitsu.delivery_fee_api.dto.StationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.ArrayList;
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
    private final XmlMapper xmlMapper;

    private static final float DEFAULT_FLOAT_VALUE = 0.0f;

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() {
        log.info("Starting weather data import job");
        try {
            String data = fetchWeatherData();
            List<City> cities = cityRepository.findAll();
            List<WeatherData> weatherDataList = processWeatherData(data, cities);
            saveWeatherData(weatherDataList);
        } catch (Exception e) {
            log.error("Error during weather data fetch: {}", e.getMessage(), e);
        }
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
    
    private List<WeatherData> processWeatherData(String data, List<City> cities) {
        List<Integer> relevantWmoCodes = cities.stream().map(City::getWmoCode).collect(Collectors.toList());
        log.info("Relevant WMO Codes: {}", relevantWmoCodes);
        return parseData(data, relevantWmoCodes);
    }
    

    private List<WeatherData> parseData(String data, List<Integer> relevantWmoCodes) {
        try {
            ObservationsDTO observations = parseXmlData(data);
            return createWeatherDataList(observations, relevantWmoCodes);
        } catch (Exception e) {
            log.error("Error parsing weather data: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    private ObservationsDTO parseXmlData(String data) throws Exception {
        return xmlMapper.readValue(data, ObservationsDTO.class);
    }
    
    private List<WeatherData> createWeatherDataList(ObservationsDTO observations, List<Integer> relevantWmoCodes) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        int observationTimestamp = Integer.parseInt(observations.getTimestamp());
    
        for (StationDTO station : observations.getStations()) {
            int wmoCode;
            try {
                wmoCode = Integer.parseInt(station.getWmocode());
            } catch (NumberFormatException e) {
                log.warn("Invalid WMO code for station {}", station.getName());
                continue;
            }

            if (!isRelevantStation(wmoCode, relevantWmoCodes)) {
                log.info("Skipping station with irrelevant WMO code: {}", wmoCode);
                continue;
            } 
            
            WeatherData weatherData = createWeatherData(station, wmoCode, observationTimestamp);
            weatherDataList.add(weatherData);
            log.info("Parsed data for station: {}", weatherData);
        }
        return weatherDataList;
    }
    
    private WeatherData createWeatherData(StationDTO station, int wmoCode, int observationTimestamp) {
        WeatherData weatherData = new WeatherData();
        weatherData.setStationName(station.getName());
        weatherData.setWmoCode(wmoCode);
        weatherData.setAirTemperature(parseFloat(station.getAirtemperature()));
        weatherData.setWindSpeed(parseFloat(station.getWindspeed()));
        weatherData.setWeatherPhenomenon(station.getPhenomenon());
        weatherData.setObservationTimestamp(observationTimestamp);
        return weatherData;
    }
    
    
    private float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return DEFAULT_FLOAT_VALUE;
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

    private boolean isRelevantStation(int wmoCode, List<Integer> relevantWmoCodes) {
        return relevantWmoCodes.contains(wmoCode);
    }
}
