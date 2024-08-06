package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.exception.NotFoundException;
import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationService {
    private final WeatherDataRepository weatherDataRepository;
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;
    private static final String FORBIDDEN_VEHICLE = "Usage of selected vehicle type is forbidden";

    public BigDecimal calculateDeliveryFee(String cityName, String vehicleTypeName, LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
            log.info("No dateTime provided. Using current time: {}", dateTime);
        }

        log.info("Calculating delivery fee for city: {}, vehicleType: {} and dateTime: {}", cityName, vehicleTypeName, dateTime);

        City city = getCityByName(cityName);
        VehicleType vehicleType = getVehicleTypeByName(vehicleTypeName);
        
        WeatherData weatherData = getWeatherData(city, dateTime);

        log.info("-------------------------");
        log.info("Main request parameters: ");
        log.info("City name of City Object: {}", cityName);
        log.info("Vehicle Type Object: {}", vehicleTypeName);
        log.info("Weather Data Object: {}", weatherData);
        log.info("Observation Timestamp: {}", weatherData.getObservationTimestamp());
        log.info("Weather Phenomenon Name: {}", weatherData.getWeatherPhenomenon());
        log.info("Current Wind Speed: {}", weatherData.getWindSpeed());
        log.info("Current Air Temperature: {}", weatherData.getAirTemperature());
        log.info("-------------------------");

        BigDecimal baseFee = fetchBaseFee(city, vehicleType, dateTime);
        BigDecimal weatherPhenomenonExtraFee = calculateWeatherPhenomenonExtraFee(weatherData, vehicleType, dateTime);
        BigDecimal airTemperatureExtraFee = calculateAirTemperatureExtraFee(weatherData, vehicleType, dateTime);
        BigDecimal windSpeedExtraFee = calculateWindSpeedExtraFee(weatherData, vehicleType, dateTime);
        log.info("weatherPhenomenonExtraFee: {}", weatherPhenomenonExtraFee);
        log.info("airTemperatureExtraFee: {}", airTemperatureExtraFee);
        log.info("windSpeedExtraFee: {}", windSpeedExtraFee);

        BigDecimal totalFee = baseFee.add(weatherPhenomenonExtraFee).add(airTemperatureExtraFee).add(windSpeedExtraFee);
        log.info("Total Fee: {}", totalFee);

        return totalFee;
    }

    private City getCityByName(String cityName) {
        return cityRepository.findByCity(cityName);
    }

    private VehicleType getVehicleTypeByName(String vehicleTypeName) {
        return vehicleTypeRepository.findByVehicleType(vehicleTypeName);
    }

    private WeatherData getWeatherData(City city, LocalDateTime dateTime) {
        Integer epochSeconds = convertToEpochSeconds(dateTime);
        List<WeatherData> weatherDataList = weatherDataRepository.findWeatherDataByWmoCodeAndTimestamp(city.getWmoCode(), epochSeconds);
        if (weatherDataList.isEmpty()) {
            throw new NotFoundException("Weather data not found for city: " + city + " and dateTime: " + dateTime);
        }
        return weatherDataRepository.findLatestByWMOCodeAsOf(city.getWmoCode(), epochSeconds);
    }
    
    private int convertToEpochSeconds(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Tallinn");
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        return Math.toIntExact(zonedDateTime.toEpochSecond());
    }

    private BigDecimal fetchBaseFee(City city, VehicleType vehicleType, LocalDateTime dateTime) {
        log.info("Fetching Base Fee for {} and {}", city.getCity(), vehicleType.getVehicleType());
        
        Optional<RegionalBaseFee> baseFeeModel = regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType);
        if (baseFeeModel.isEmpty()) {
            throw new NotFoundException("Base fee not found for given City and VehicleType");
        }
        BigDecimal baseFee = regionalBaseFeeRepository.fetchLatestBaseFee(city.getId(), vehicleType.getId(), dateTime);
        log.info("Base Fee: {}", baseFee);
        return baseFee;
    }

    private BigDecimal calculateWeatherPhenomenonExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        log.info("Calculating Weather Phenomenon Extra Fee for {} and {}", weatherData.getWeatherPhenomenon(), vehicleType.getVehicleType());
        if (!vehicleType.getExtraFeeApplicable() ) {
            log.info("No WPEF applicable for selected vehicle type or weather phenomenon");
            return BigDecimal.ZERO;
        }

        if (weatherData.getWeatherPhenomenon() == null || weatherData.getWeatherPhenomenon().trim().isEmpty()) {
            log.info("No weather phenomenon in the weather data that would incur extra fee");
            return BigDecimal.ZERO;
        }


        WeatherPhenomenonType weatherPhenomenon = weatherPhenomenonTypeRepository.findByPhenomenon(weatherData.getWeatherPhenomenon());
        String weatherPhenomenonCategory = weatherPhenomenon.getWeatherPhenomenonCategory();

        if (weatherPhenomenonCategory.equals(WeatherPhenomenonType.CATEGORY_NONE)) {
            log.info("No extra fee for governing weather phenomenon");
            return BigDecimal.ZERO;
        }

        WeatherPhenomenonExtraFee fee = weatherPhenomenonExtraFeeRepository
            .findLatestByPhenomenonCategoryCodeVehicleTypeAndQueryTime(weatherPhenomenonCategory, vehicleType.getId(), dateTime);

        if (fee == null) {
            log.info("No WPEF for selected weather phenomenon and vehicle type");
            return BigDecimal.ZERO;
        }

        if (fee.getForbidden()) {
            throw new VehicleUsageForbiddenException(FORBIDDEN_VEHICLE);
        }
        log.info("WPEF: {}", fee.getExtraFee());
        return fee.getExtraFee();
    }

    private BigDecimal calculateAirTemperatureExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        log.info("Calculating Air Temperature Extra Fee for {} and {}", weatherData.getWeatherPhenomenon(), vehicleType.getVehicleType());
        
        if (!vehicleType.getExtraFeeApplicable()) {
            log.info("No ATEF applicable for selected vehicle type or weather phenomenon");
            return BigDecimal.ZERO;
        }

        AirTemperatureExtraFee fee = airTemperatureExtraFeeRepository
            .findLatestByTemperatureAndVehicleTypeAndQueryTime(weatherData.getAirTemperature(), vehicleType.getId(), dateTime);

        if (fee == null) {
            log.info("No ATEF for selected temperature and vehicle type");
            return BigDecimal.ZERO;
        }
        
        log.info("ATEF: {}", fee.getExtraFee());
        return fee.getExtraFee();
    }

    private BigDecimal calculateWindSpeedExtraFee(WeatherData weatherData, VehicleType vehicleType, LocalDateTime dateTime) {
        log.info("Calculating Wind Speed Extra Fee for wind speed {} and {}", weatherData.getWindSpeed(), vehicleType.getVehicleType());
        if (!vehicleType.getExtraFeeApplicable()) {
            log.info("No WSEF applicable for selected vehicle type or weather phenomenon");
            return BigDecimal.ZERO;
        }

        WindSpeedExtraFee fee = windSpeedExtraFeeRepository
            .findLatestByWindSpeedAndVehicleTypeAndQueryTime(weatherData.getWindSpeed(), vehicleType.getId(), dateTime);

        if (fee == null) {
            log.info("No WSEF for selected wind speed and vehicle type");
            return BigDecimal.ZERO;
        }

        if (fee.getForbidden()) {
            throw new VehicleUsageForbiddenException(FORBIDDEN_VEHICLE);
        }

        log.info("WSEF: {}", fee.getExtraFee());
        return fee.getExtraFee();
    }
}