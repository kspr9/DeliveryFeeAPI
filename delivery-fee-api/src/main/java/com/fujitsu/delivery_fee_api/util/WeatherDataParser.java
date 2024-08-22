package com.fujitsu.delivery_fee_api.util;

import com.fujitsu.delivery_fee_api.dto.ObservationsDTO;
import com.fujitsu.delivery_fee_api.dto.StationDTO;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataParser {
    
    private final XmlMapper xmlMapper;
    private static final float DEFAULT_FLOAT_VALUE = 0.0f;

    public List<WeatherData> parseWeatherData(String xmlData, List<Integer> relevantWmoCodes) {
        try {
            ObservationsDTO observations = parseXmlData(xmlData);
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

    private boolean isRelevantStation(int wmoCode, List<Integer> relevantWmoCodes) {
        return relevantWmoCodes.contains(wmoCode);
    }
}
