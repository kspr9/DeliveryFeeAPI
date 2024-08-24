package com.fujitsu.delivery_fee_api.config;

import com.fujitsu.delivery_fee_api.service.WeatherDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataImportScheduler {

    private final WeatherDataService weatherDataService;

    @Scheduled(cron = "${weather.import.cron}")
    public void scheduleWeatherDataImport() {
        
        weatherDataService.importWeatherData();
        
    }
}