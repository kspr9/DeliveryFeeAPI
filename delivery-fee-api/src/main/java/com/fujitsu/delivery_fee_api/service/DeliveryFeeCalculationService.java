package com.fujitsu.delivery_fee_api.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.RegionalBaseFeeRepository;
import com.fujitsu.delivery_fee_api.repository.VehicleTypeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherDataRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonTypeRepository;
import com.fujitsu.delivery_fee_api.repository.WindSpeedExtraFeeRepository;

// import java.math.BigDecimal;

@Service
public class DeliveryFeeCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeCalculationService.class);

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
    public Float calculateDeliveryFee(String city, String vehicleType, LocalDateTime dateTime) throws Exception {
        logger.info("dateTime.now() is: {}", dateTime);
        // Use dateTime if provided, otherwise default to current system time
        if (dateTime == null) {
            dateTime = LocalDateTime.now(); // Default to current system time if dateTime is not provided
            logger.info("dateTime.now() is: {}", dateTime);
        }


        logger.info("Calculating delivery fee for city: {}, vehicleType: {} and dateTime: {}", city, vehicleType, dateTime.toLocalDate());

        Float totalFee = 0.0f;
        Float wpef = 0.0f;
        Float atef = 0.0f;
        Float wsef = 0.0f;

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
        
        logger.info("-------------------------");
        logger.info("Main request parameters: ");
        logger.info("City name of City Object: {}", cityObj.getCity());
        logger.info("Vehicle Type Object: {}", vehicleTypeObj.getVehicleType());
        logger.info("Weather Data Object: {}", latestWeatherInCityObj.getStationName());
        logger.info("Observation Timestamp: {}", latestWeatherInCityObj.getObservationTimestampAsLocalDateTime());
        logger.info("Weather Phenomenon Name: {}", weatherPhenomenonName);
        logger.info("Current Wind Speed: {}", latestWeatherInCityObj.getWindSpeed());
        logger.info("Current Air Temperature: {}", latestWeatherInCityObj.getAirTemperature());
        logger.info("-------------------------");


        // HANDLE WEATHER PHENOMENON EXTRA FEE
        // 
        // WP Extra Fees are only applicable if 
        // there is a weather phenomenon in the latest weather data
        // and the vehicle type belongs to extraFeeApplicable category
        if (weatherPhenomenonName != null && !weatherPhenomenonName.trim().isEmpty() && vehicleTypeObj.getExtraFeeApplicable() == true) {
            logger.info("ENTERING - Weather Phenomenon Extra Fee");

            try {
                wpef += calculateWPEF(weatherPhenomenonName, vehicleTypeObj, dateTime);
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
            logger.info("ENTERING - Air Temperature Extra Fee");
            // get the air temperature from the latest weather data - latestWeatherInCityObj
            Float currentAirTemperature = latestWeatherInCityObj.getAirTemperature();
            logger.info("Current Air Temperature: {}", currentAirTemperature);

            atef += calculateATEF(vehicleTypeObj, currentAirTemperature, dateTime);
        }


        // HANDLE WIND SPEED EXTRA FEE
        //
        // Wind Speed Extra Fees are only applicable if 
        // the vehicle type belongs to extraFeeApplicable category
        if (vehicleTypeObj.getExtraFeeApplicable() == true) {
            logger.info("ENTERING - Wind Speed Extra Fee");
            // get the wind speed from the latest weather data - latestWeatherInCityObj
            Float currentWindSpeed = latestWeatherInCityObj.getWindSpeed();
            logger.info("Current Wind Speed: {}", currentWindSpeed);

            // use try-catch block to handle exceptions
            try {
                wsef += calculateWSEF(vehicleTypeObj, currentWindSpeed, dateTime);
            } catch (Exception e) {
                // Handle the exception, perhaps log it or rethrow as necessary
                throw e;
            }
        }


        // Fetch base fee
        Float baseFee = fetchBaseFee(cityObj, vehicleTypeObj, dateTime);
        logger.info(" ");
        logger.info("Before final return");
        logger.info("Base Fee: {}", baseFee);
        logger.info("WPEF: {}", wpef);
        logger.info("ATEF: {}", atef);
        logger.info("WSEF: {}", wsef);
        logger.info("-------------------------");

        totalFee += baseFee + atef + wsef + wpef;
        logger.info("Total Fee: {}", totalFee);

        return totalFee;
    }

    private Float fetchBaseFee(City cityObj, VehicleType vehicleTypeObj, LocalDateTime dateTime) {
        logger.info("ENTERING - Fetch Base Fee for {} and {}", cityObj.getCity(), vehicleTypeObj.getVehicleType());
        // Fetch and calculate the base fee based on city and vehicle type
        Float rbf = regionalBaseFeeRepository.fetchLatestBaseFee(cityObj.getId(), vehicleTypeObj.getId(), dateTime);
        logger.info("EXITING -- Base Fee: {}", rbf);
        logger.info("-------------------------");
        return rbf;
    }
    
    private Float calculateWPEF(String weatherPhenomenonName, VehicleType vehicleTypeObj, LocalDateTime dateTime) throws Exception {
        logger.info("ENTERING - Calculate Weather Phenomenon Extra Fee for {} and {} at {}", weatherPhenomenonName, vehicleTypeObj.getVehicleType(), dateTime.toLocalDate());

        Float wpef = 0.0f;

        // fetch the WeatherPhenomenonType object based on weatherPhenomenonName
        WeatherPhenomenonType weatherPhenomenonObj = weatherPhenomenonTypeRepository.findByPhenomenon(weatherPhenomenonName);
        // Find the weather phenomenon extra fee category and assign the phenomenonCategoryCode to weatherPhenomenonCategory variable
        String weatherPhenomenonCategory = weatherPhenomenonObj.getWeatherPhenomenonCategory();
        logger.info("Weather Phenomenon Category: {}", weatherPhenomenonCategory);

        // if weatherPhenomenonCategory is not null, ie WPEF is applicable only if there is weather phenomenon EF category for given phenomenon 
        if (weatherPhenomenonCategory != null) {
            logger.info("Calculating Weather Phenomenon Extra Fee");

            // fetch the WeatherPhenomenonExtraFee object based on weatherPhenomenonCategory, vehicleType and dateTime
            WeatherPhenomenonExtraFee weatherPhenomenonExtraFeeObj = weatherPhenomenonExtraFeeRepository
                                                                    .findLatestByPhenomenonCategoryCodeVehicleTypeAndQueryTime(weatherPhenomenonCategory, vehicleTypeObj.getId(), dateTime);
                
            // Throw an exception if the weather phenomenon extra fee category is forbidden for the provided vehicle type
            if (weatherPhenomenonExtraFeeObj.getForbidden()) {
                logger.info("WPEF not added to total fee: {}", weatherPhenomenonExtraFeeObj.getExtraFee());
                logger.info("EXITING -- Weather Phenomenon Extra Fee is forbidden for this vehicle type: {}", vehicleTypeObj);
                logger.info("-------------------------");
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
    
            // Get the weather phenomenon extra fee from the matching fee instance
            wpef += weatherPhenomenonExtraFeeObj.getExtraFee();
        } else {
            logger.info("weatherPhenomenonCategory was null, so WP no extra fees applicable for any vehicle type");
        }

        logger.info("EXITING -- WPEF: {}", wpef);
        logger.info("-------------------------");
        return wpef;
    }
    
    
    private Float calculateATEF(VehicleType vehicleTypeObj, Float airTemperature, LocalDateTime dateTime) {
        logger.info("ENTERING - Calculate Air Temperature Extra Fee for temperature {} and {}", airTemperature, vehicleTypeObj.getVehicleType());
        
        // Fetch air temperature extra fee instance based on temperature and vehicle type
        AirTemperatureExtraFee airTemperatureExtraFeeObj = airTemperatureExtraFeeRepository.findLatestByTemperatureAndVehicleTypeAndQueryTime(airTemperature, vehicleTypeObj.getId(), dateTime);

        // If no applicable fees are found, return 0.0f
        if (airTemperatureExtraFeeObj == null) {
            logger.info("No applicable extra fees found for given air temperature");
            logger.info("EXITING -- ATEF.isEmpty: {}", 0.0f);
            logger.info("-------------------------");
            return 0.0f;  // Return 0.0f if no fees are applicable
        }
        
        // Get the air temperature extra fee from the matching fee instance
        Float atef = airTemperatureExtraFeeObj.getExtraFee();
        logger.info("EXITING -- ATEF: {}", atef);
        logger.info("-------------------------");
        return atef;

    }

    private Float calculateWSEF(VehicleType vehicleTypeObj, Float windSpeed, LocalDateTime dateTime) throws Exception {
            logger.info("ENTERING - Calculate Wind Speed Extra Fee for windSpeed {} and {}", windSpeed, vehicleTypeObj.getVehicleType());
        
        // Fetch wind speed extra fee instance based on wind speed and vehicle type
        WindSpeedExtraFee windSpeedExtraFeeObj = windSpeedExtraFeeRepository.findLatestByWindSpeedAndVehicleTypeAndQueryTime(windSpeed, vehicleTypeObj.getId(), dateTime);
        
        if (windSpeedExtraFeeObj == null) {
            logger.info("No applicable extra fees found for given wind speed");
            logger.info("EXITING -- WSEF.isEmpty: {}", 0.0f);
            logger.info("-------------------------");
            return 0.0f;  // Return 0.0f if no fees are applicable
        }
        

        // Throw an exception if the wind speed extra fee category is forbidden for the provided vehicle type
        if (windSpeedExtraFeeObj.getForbidden()) {
            logger.info("WSEF not added: {}", windSpeedExtraFeeObj.getExtraFee());
            logger.info("EXITING -- Wind Speed Extra Fee is forbidden for this vehicle type: {}", vehicleTypeObj.getVehicleType());
            logger.info("-------------------------");
            throw new Exception("Usage of selected vehicle type is forbidden");
        }
        
        // Get the wind speed extra fee from the matching fee instance
        Float wsef = windSpeedExtraFeeObj.getExtraFee();
        logger.info("EXITING -- WSEF: {}", wsef);
        logger.info("-------------------------");
        return wsef; 
    }
    

}
