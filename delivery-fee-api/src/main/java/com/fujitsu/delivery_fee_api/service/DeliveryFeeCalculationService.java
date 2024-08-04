package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationService {

    
    private final WeatherDataRepository weatherDataRepository;
    
    // define all data type repositories
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;

    // define all fee repositories
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;


    @Autowired
    public DeliveryFeeCalculationService(WeatherDataRepository weatherDataRepository, VehicleTypeRepository vehicleTypeRepository, CityRepository cityRepository, WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository, RegionalBaseFeeRepository regionalBaseFeeRepository, AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository, WindSpeedExtraFeeRepository windSpeedExtraFeeRepository, WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository) {
        this.weatherDataRepository = weatherDataRepository;
        this.cityRepository = cityRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.weatherPhenomenonTypeRepository = weatherPhenomenonTypeRepository;
        this.regionalBaseFeeRepository = regionalBaseFeeRepository;
        this.airTemperatureExtraFeeRepository = airTemperatureExtraFeeRepository;
        this.windSpeedExtraFeeRepository = windSpeedExtraFeeRepository;
        this.weatherPhenomenonExtraFeeRepository = weatherPhenomenonExtraFeeRepository;
    }

    /**
     * Calculates the delivery fee based on the city, vehicle type, and optional date/time.
     *
     * @param  city         the name of the city
     * @param  vehicleType  the type of the vehicle
     * @param  dateTime     the date/time of the delivery (optional, defaults to current system time)
     * @return               the calculated total delivery fee
     * @throws Exception    if there is an error during the calculation
     */
    public BigDecimal calculateDeliveryFee(String city, String vehicleType, LocalDateTime dateTime) throws Exception {
        log.info("dateTime.now() is: {}", dateTime);
        // Use dateTime if provided, otherwise default to current system time
        if (dateTime == null) {
            dateTime = LocalDateTime.now(); // Default to current system time if dateTime is not provided
            log.info("dateTime.now() is: {}", dateTime);
        }


        log.info("Calculating delivery fee for city: {}, vehicleType: {} and dateTime: {}", city, vehicleType, dateTime.toLocalDate());

        BigDecimal wpef = BigDecimal.ZERO;
        BigDecimal atef = BigDecimal.ZERO;
        BigDecimal wsef = BigDecimal.ZERO;

        // Fetch City object based on supplied city name
        City cityObj = cityRepository.findByCity(city);
        
        // Fetch VehicleType object based on supplied vehicle type
        VehicleType vehicleTypeObj = vehicleTypeRepository.findByVehicleType(vehicleType);
        
        // converting dateTime to epoch seconds since weatherData observation timestamp is int
        ZoneId zoneId = ZoneId.of("Europe/Tallinn");
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        long epochSecondsLong = zonedDateTime.toEpochSecond();
        // Convert long to int
        int epochSeconds = Math.toIntExact(epochSecondsLong);
        
        // Fetch weather data corresponding to the weatherstation as per WMO code from the City object and most recent observation corresponding to the supplied dateTime
        WeatherData latestWeatherInCityObj = weatherDataRepository.findLatestByWMOCodeAsOf(cityObj.getWmoCode(), epochSeconds);
        
        // Fetch the name of weather phenomenon in the latest weather data
        String weatherPhenomenonName = latestWeatherInCityObj.getWeatherPhenomenon();
        
        log.info("-------------------------");
        log.info("Main request parameters: ");
        log.info("City name of City Object: {}", cityObj.getCity());
        log.info("Vehicle Type Object: {}", vehicleTypeObj.getVehicleType());
        log.info("Weather Data Object: {}", latestWeatherInCityObj.getStationName());
        log.info("Observation Timestamp: {}", latestWeatherInCityObj.getObservationTimestampAsLocalDateTime());
        log.info("Weather Phenomenon Name: {}", weatherPhenomenonName);
        log.info("Current Wind Speed: {}", latestWeatherInCityObj.getWindSpeed());
        log.info("Current Air Temperature: {}", latestWeatherInCityObj.getAirTemperature());
        log.info("-------------------------");


        // HANDLE WEATHER PHENOMENON EXTRA FEE
        // 
        // WP Extra Fees are only applicable if 
        // there is a weather phenomenon in the latest weather data
        // and the vehicle type belongs to extraFeeApplicable category
        if (weatherPhenomenonName != null && !weatherPhenomenonName.trim().isEmpty() && vehicleTypeObj.getExtraFeeApplicable() == true) {
            log.info("ENTERING - Weather Phenomenon Extra Fee");

            try {
                wpef = calculateWPEF(weatherPhenomenonName, vehicleTypeObj, dateTime);
            } catch (Exception e) {
                // Handle the exception, perhaps log it or rethrow as necessary
                throw e;
            }

        }
        

        // HANDLE AIR TEMPERATURE EXTRA FEE
        //
        // Air Temperature Extra Fees are only applicable if 
        // the vehicle type belongs to extraFeeApplicable category
        if (vehicleTypeObj.getExtraFeeApplicable() == true) {
            log.info("ENTERING - Air Temperature Extra Fee");
            // get the air temperature from the latest weather data - latestWeatherInCityObj
            Float currentAirTemperature = latestWeatherInCityObj.getAirTemperature();
            log.info("Current Air Temperature: {}", currentAirTemperature);

            atef = calculateATEF(vehicleTypeObj, currentAirTemperature, dateTime);
        }


        // HANDLE WIND SPEED EXTRA FEE
        //
        // Wind Speed Extra Fees are only applicable if 
        // the vehicle type belongs to extraFeeApplicable category
        if (vehicleTypeObj.getExtraFeeApplicable() == true) {
            log.info("ENTERING - Wind Speed Extra Fee");
            // get the wind speed from the latest weather data - latestWeatherInCityObj
            Float currentWindSpeed = latestWeatherInCityObj.getWindSpeed();
            log.info("Current Wind Speed: {}", currentWindSpeed);

            // use try-catch block to handle exceptions
            try {
                wsef = calculateWSEF(vehicleTypeObj, currentWindSpeed, dateTime);
            } catch (Exception e) {
                // Handle the exception, perhaps log it or rethrow as necessary
                throw e;
            }
        }


        // Fetch base fee
        BigDecimal baseFee = fetchBaseFee(cityObj, vehicleTypeObj, dateTime);
        log.info(" ");
        log.info("Before final return");
        log.info("Base Fee: {}", baseFee);
        log.info("WPEF: {}", wpef);
        log.info("ATEF: {}", atef);
        log.info("WSEF: {}", wsef);
        log.info("-------------------------");

        BigDecimal totalFee = baseFee.add(atef).add(wsef).add(wpef);
        log.info("Total Fee: {}", totalFee);

        return totalFee;
    }

    private BigDecimal fetchBaseFee(City cityObj, VehicleType vehicleTypeObj, LocalDateTime dateTime) {
        log.info("ENTERING - Fetch Base Fee for {} and {}", cityObj.getCity(), vehicleTypeObj.getVehicleType());
        // Fetch and calculate the base fee based on city and vehicle type
        BigDecimal rbf = regionalBaseFeeRepository.fetchLatestBaseFee(cityObj.getId(), vehicleTypeObj.getId(), dateTime);
        log.info("EXITING -- Base Fee: {}", rbf);
        log.info("-------------------------");
        return rbf;
    }
    
    private BigDecimal calculateWPEF(String weatherPhenomenonName, VehicleType vehicleTypeObj, LocalDateTime dateTime) throws Exception {
        log.info("ENTERING - Calculate Weather Phenomenon Extra Fee for {} and {} at {}", weatherPhenomenonName, vehicleTypeObj.getVehicleType(), dateTime.toLocalDate());

        BigDecimal wpef = BigDecimal.ZERO;

        // fetch the WeatherPhenomenonType object based on weatherPhenomenonName
        WeatherPhenomenonType weatherPhenomenonObj = weatherPhenomenonTypeRepository.findByPhenomenon(weatherPhenomenonName);
        // Find the weather phenomenon extra fee category and assign the phenomenonCategoryCode to weatherPhenomenonCategory variable
        String weatherPhenomenonCategory = weatherPhenomenonObj.getWeatherPhenomenonCategory();
        log.info("Weather Phenomenon Category: {}", weatherPhenomenonCategory);

        // if weatherPhenomenonCategory is not null, ie WPEF is applicable only if there is weather phenomenon EF category for given phenomenon 
        if (weatherPhenomenonCategory != "None") {
            log.info("Calculating Weather Phenomenon Extra Fee");

            // fetch the WeatherPhenomenonExtraFee object based on weatherPhenomenonCategory, vehicleType and dateTime
            WeatherPhenomenonExtraFee weatherPhenomenonExtraFeeObj = weatherPhenomenonExtraFeeRepository
                                                                    .findLatestByPhenomenonCategoryCodeVehicleTypeAndQueryTime(weatherPhenomenonCategory, vehicleTypeObj.getId(), dateTime);
                
            // Throw an exception if the weather phenomenon extra fee category is forbidden for the provided vehicle type
            if (weatherPhenomenonExtraFeeObj.getForbidden()) {
                log.info("WPEF not added to total fee: {}", weatherPhenomenonExtraFeeObj.getExtraFee());
                log.info("EXITING -- Weather Phenomenon Extra Fee is forbidden for this vehicle type: {}", vehicleTypeObj);
                log.info("-------------------------");
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
    
            // Get the weather phenomenon extra fee from the matching fee instance
            wpef = weatherPhenomenonExtraFeeObj.getExtraFee();
        } else {
            log.info("weatherPhenomenonCategory was None, so WP no extra fees applicable for any vehicle type");
        }

        log.info("EXITING -- WPEF: {}", wpef);
        log.info("-------------------------");
        return wpef;
    }
    
    
    private BigDecimal calculateATEF(VehicleType vehicleTypeObj, Float airTemperature, LocalDateTime dateTime) {
        log.info("ENTERING - Calculate Air Temperature Extra Fee for temperature {} and {}", airTemperature, vehicleTypeObj.getVehicleType());
        
        // Fetch air temperature extra fee instance based on temperature and vehicle type
        AirTemperatureExtraFee airTemperatureExtraFeeObj = airTemperatureExtraFeeRepository.findLatestByTemperatureAndVehicleTypeAndQueryTime(airTemperature, vehicleTypeObj.getId(), dateTime);

        // If no applicable fees are found, return BigDecimal.ZERO;
        if (airTemperatureExtraFeeObj == null) {
            log.info("No applicable extra fees found for given air temperature");
            log.info("EXITING -- ATEF.isEmpty: {}", BigDecimal.ZERO);
            log.info("-------------------------");
            return BigDecimal.ZERO;  // Return 0.0f if no fees are applicable
        }
        
        // Get the air temperature extra fee from the matching fee instance
        BigDecimal atef = airTemperatureExtraFeeObj.getExtraFee();
        log.info("EXITING -- ATEF: {}", atef);
        log.info("-------------------------");
        return atef;

    }

    private BigDecimal calculateWSEF(VehicleType vehicleTypeObj, Float windSpeed, LocalDateTime dateTime) throws Exception {
            log.info("ENTERING - Calculate Wind Speed Extra Fee for windSpeed {} and {}", windSpeed, vehicleTypeObj.getVehicleType());
        
        // Fetch wind speed extra fee instance based on wind speed and vehicle type
        WindSpeedExtraFee windSpeedExtraFeeObj = windSpeedExtraFeeRepository.findLatestByWindSpeedAndVehicleTypeAndQueryTime(windSpeed, vehicleTypeObj.getId(), dateTime);
        
        if (windSpeedExtraFeeObj == null) {
            log.info("No applicable extra fees found for given wind speed");
            log.info("EXITING -- WSEF.isEmpty: {}", BigDecimal.ZERO);
            log.info("-------------------------");
            return BigDecimal.ZERO;  // Return 0.0f if no fees are applicable
        }
        

        // Throw an exception if the wind speed extra fee category is forbidden for the provided vehicle type
        if (windSpeedExtraFeeObj.getForbidden()) {
            log.info("WSEF not added: {}", windSpeedExtraFeeObj.getExtraFee());
            log.info("EXITING -- Wind Speed Extra Fee is forbidden for this vehicle type: {}", vehicleTypeObj.getVehicleType());
            log.info("-------------------------");
            throw new Exception("Usage of selected vehicle type is forbidden");
        }
        
        // Get the wind speed extra fee from the matching fee instance
        BigDecimal wsef = windSpeedExtraFeeObj.getExtraFee();
        log.info("EXITING -- WSEF: {}", wsef);
        log.info("-------------------------");
        return wsef; 
    }
    

}
