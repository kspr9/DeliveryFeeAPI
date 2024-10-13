package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
import com.fujitsu.delivery_fee_api.util.TimeUtils;
import com.fujitsu.delivery_fee_api.util.WeatherDataParser;
import com.fujitsu.delivery_fee_api.dto.ObservationsDTO;
import com.fujitsu.delivery_fee_api.dto.WeatherDataDTO;
import com.fujitsu.delivery_fee_api.mapper.WeatherDataMapper;
import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.WeatherData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;
    
    private final RestTemplate restTemplate;
    private final WeatherDataRepository weatherDataRepository;
    private final CityRepository cityRepository;
    private final WeatherDataParser weatherDataParser;
    private final WeatherDataMapper weatherDataMapper;
    private final TimeUtils timeUtils;

    public void importWeatherData() {
        String xmlData = fetchWeatherData();
        
        List<City> cities = cityRepository.findAll();
        List<Integer> relevantWmoCodes = cities.stream().map(City::getWmoCode).collect(Collectors.toList());
        ObservationsDTO relevantObservationsDTO = weatherDataParser.parseWeatherDataToDTO(xmlData, relevantWmoCodes);
        
        List<WeatherDataDTO> weatherDataDTOs = relevantObservationsDTO.getStations().stream()
            .map(station -> weatherDataMapper.stationDtoToWeatherDataDto(station, relevantObservationsDTO.getTimestamp()))
            .collect(Collectors.toList());

        saveAllWeatherData(weatherDataDTOs);
    }
    
    private String fetchWeatherData() {
        ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Received weather data successfully");
            return response.getBody();
        } else {
            log.error("Failed to fetch weather data: HTTP Status {}", response.getStatusCode());
            throw new RuntimeException("Failed to fetch weather data");
        }
    }

    private void saveAllWeatherData(List<WeatherDataDTO> weatherDataDTOs) {
        for (WeatherDataDTO weatherDataDTO : weatherDataDTOs) {
            log.info("Saving weather data for station: {}", weatherDataDTO.getStationName());
            saveWeatherData(weatherDataDTO);
        }
        log.info("Weather data saved successfully");
    }

    public WeatherDataDTO getWeatherDataByCityId(Long id) {
        return weatherDataRepository.findById(id)
            .map(weatherDataMapper::toDto)
            .orElse(null);
    }

    public WeatherDataDTO getWeatherDataByCityName(String cityName, LocalDateTime dateTime) {
        dateTime = TimeUtils.getCurrentDateTimeIfNull(dateTime);

        Integer epochSeconds = timeUtils.convertToEpochSeconds(dateTime);

        City city = cityRepository.findByName(cityName);

        return weatherDataRepository.findLatestByWMOCodeAsOfOpt(city.getWmoCode(), epochSeconds)
            .map(weatherDataMapper::toDto)
            .orElse(null);
    }
    
  
   
    public WeatherDataDTO saveWeatherData(WeatherDataDTO weatherDataDTO) {
        WeatherData weatherData = weatherDataMapper.toEntity(weatherDataDTO);
        WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
        return weatherDataMapper.toDto(savedWeatherData);
    }

    public List<WeatherDataDTO> getAllWeatherData() {
        return weatherDataRepository.findAll().stream()
            .map(weatherDataMapper::toDto)
            .collect(Collectors.toList());
    }
}