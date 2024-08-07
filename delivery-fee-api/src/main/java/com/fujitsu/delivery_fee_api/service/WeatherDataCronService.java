package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.StringReader;
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

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() {
        log.info("Starting weather data import job");
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Received data successfully");
                // Get a list of cities to filter the weather data based on WMOCodes
                List<City> cities = cityRepository.findAll();
                List<Integer> relevantWmoCodes = cities.stream().map(City::getWmoCode).collect(Collectors.toList());
                log.info("Relevant WMO Codes: {}", relevantWmoCodes);
                List<WeatherData> weatherDataList = parseData(response.getBody(), relevantWmoCodes);
                // log.info("Fetched data successfully: {}", response.getBody());
                saveWeatherData(weatherDataList);
            } else {
                log.error("Failed to fetch data: HTTP Status {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error during weather data fetch: {}", e.getMessage(), e);
        }
    }

    private List<WeatherData> parseData(String data, List<Integer> relevantWmoCodes) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(data)));
            doc.getDocumentElement().normalize();
    
            NodeList stationList = doc.getElementsByTagName("station");
    
            for (int i = 0; i < stationList.getLength(); i++) {
                Element stationElement = (Element) stationList.item(i);
                String wmoCodeStr = getTextContent(stationElement, "wmocode");
                log.info("WMO Code: {}", wmoCodeStr);
                
                if (wmoCodeStr.isEmpty()) {
                    log.warn("Missing WMO code for station element: {}", stationElement);
                    continue;
                }

                int wmoCode = Integer.parseInt(wmoCodeStr);
                log.info("Processing station with WMO code: {}", wmoCode);

                if (relevantWmoCodes.contains(wmoCode)) {
                    String name = getTextContent(stationElement, "name");
                    float airTemperature = parseFloat(getTextContent(stationElement, "airtemperature"));
                    float windSpeed = parseFloat(getTextContent(stationElement, "windspeed"));
                    String weatherPhenomenon = getTextContent(stationElement, "phenomenon");
                    int observationTimestamp = Integer.parseInt(doc.getDocumentElement().getAttribute("timestamp"));
                    
                    log.info("Parsed data for station - Name: {}, AirTemperature: {}, WindSpeed: {}, WeatherPhenomenon: {}, ObservationTimestamp: {}",
                            name, airTemperature, windSpeed, weatherPhenomenon, observationTimestamp);

                    WeatherData weatherData = new WeatherData();
                    weatherData.setStationName(name);
                    weatherData.setWmoCode(wmoCode);
                    weatherData.setAirTemperature(airTemperature);
                    weatherData.setWindSpeed(windSpeed);
                    weatherData.setWeatherPhenomenon(weatherPhenomenon);
                    weatherData.setObservationTimestamp(observationTimestamp);
                    log.info("Station: {}, WMO Code: {}, Air Temperature: {}, Wind Speed: {}, Weather Phenomenon: {}, Observation Timestamp: {}", name, wmoCode, airTemperature, windSpeed, weatherPhenomenon, observationTimestamp);
                    weatherDataList.add(weatherData);
                } else {
                    log.info("Skipping station with irrelevant WMO code: {}", wmoCode);
                }
            }
        } catch (Exception e) {
            log.error("Error parsing weather data: {}", e.getMessage(), e);
        }
        return weatherDataList;
    }
    
    private String getTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
    
    private float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0.0f;  // Default value if parsing fails
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






// Example: Parse XML data and convert to WeatherData entities
        // Implementation depends on the XML structure
        // Results should be stored for Tallinn, Tartu, and Pärnu weather stations
        // wmo codes are used to identify the weather stations

        // example data structure from https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php
        // <observations timestamp="1717426164">
        //     <station>
        //         <name>Tallinn-Harku</name>
        //         <wmocode>26038</wmocode>
        //         <longitude>24.602891666624284</longitude>
        //         <latitude>59.398122222355134</latitude>
        //         <phenomenon/>
        //         <visibility>35.0</visibility>
        //         <precipitations>0</precipitations>
        //         <airpressure>1004.7</airpressure>
        //         <relativehumidity>62</relativehumidity>
        //         <airtemperature>21.7</airtemperature>
        //         <winddirection>265</winddirection>
        //         <windspeed>3.5</windspeed>
        //         <windspeedmax>6.3</windspeedmax>
        //         <waterlevel/>
        //         <waterlevel_eh2000/>
        //         <watertemperature/>
        //         <uvindex>2.8</uvindex>
        //         <sunshineduration>421</sunshineduration>
        //         <globalradiation>642</globalradiation>
        //     </station>  
        // </observations>