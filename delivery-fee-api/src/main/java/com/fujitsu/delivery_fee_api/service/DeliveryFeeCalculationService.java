package com.fujitsu.delivery_fee_api.service;

import java.util.List;
import java.util.Set;

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

    public Float calculateDeliveryFee(String city, String vehicleType) throws Exception {
        logger.info("Calculating delivery fee for city: {}, vehicleType: {}", city, vehicleType);

        Float totalFee = 0.0f;
        Float wpef = 0.0f;
        Float atef = 0.0f;
        Float wsef = 0.0f;

        // Fetch City object based on supplied city name
        City cityObj = cityRepository.findByCity(city);
        
        // Fetch VehicleType object based on supplied vehicle type
        VehicleType vehicleTypeObj = vehicleTypeRepository.findByVehicleType(vehicleType);
        
        // Fetch weather data corresponding to the weatherstation as per WMO code from the City object 
        WeatherData latestWeatherInCityObj = weatherDataRepository.findLatestByWMOCode(cityObj.getWmoCode());
        
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
        if (weatherPhenomenonName != null && vehicleTypeObj.getExtraFeeApplicable() == true) {
            logger.info("ENTERING - Weather Phenomenon Extra Fee");
            // TODO:  check if the weather phenomenon belongs to any EF category, if not then don't perform any calculation

            try {
                wpef += calculateWPEF(weatherPhenomenonName, vehicleTypeObj);
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

            atef += calculateATEF(vehicleTypeObj, currentAirTemperature);
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
                wsef += calculateWSEF(vehicleTypeObj, currentWindSpeed);
            } catch (Exception e) {
                // Handle the exception, perhaps log it or rethrow as necessary
                throw e;
            }
        }


        // Fetch base fee
        Float baseFee = fetchBaseFee(cityObj, vehicleTypeObj);
        logger.info("-------------------------");
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

    private Float fetchBaseFee(City cityObj, VehicleType vehicleTypeObj) {
        logger.info("ENTERING - Fetch Base Fee");
        // Fetch and calculate the base fee based on city and vehicle type
        Float rbf = regionalBaseFeeRepository.fetchBaseFee(cityObj, vehicleTypeObj);
        logger.info("EXITING -- Base Fee: {}", rbf);
        logger.info("-------------------------");
        return rbf;
    }
    
    private Float calculateWPEF(String weatherPhenomenonName, VehicleType vehicleTypeObj) throws Exception {
        logger.info("ENTERING - Calculate Weather Phenomenon Extra Fee");
        Float wpef = 0.0f;

        // fetch the WeatherPhenomenonType object based on weatherPhenomenonName
        WeatherPhenomenonType weatherPhenomenonObj = weatherPhenomenonTypeRepository.findByPhenomenon(weatherPhenomenonName);
        // Find the weather phenomenon extra fee category and assign the phenomenonCategoryCode to weatherPhenomenonCategory variable
        String weatherPhenomenonCategory = weatherPhenomenonObj.getWeatherPhenomenonCategory();
        logger.info("Weather Phenomenon Category: {}", weatherPhenomenonCategory);

        // if weatherPhenomenonCategory is not null, ie WPEF is applicable only if there is weather phenomenon EF category for given phenomenon 
        if (weatherPhenomenonCategory != null) {
            logger.info("Calculating Weather Phenomenon Extra Fee");
            // fetch the WeatherPhenomenonExtraFee object based on weatherPhenomenonCategory
            WeatherPhenomenonExtraFee weatherPhenomenonExtraFeeObj = weatherPhenomenonExtraFeeRepository.findByPhenomenonCategoryCode(weatherPhenomenonCategory);
            // fetch vehicle types applicable for the weather phenomenon extra fee category
            Set<VehicleType> applicableVehicles = weatherPhenomenonExtraFeeObj.getApplicableVehicles();
    
            // Throw an exception if the weather phenomenon extra fee category is forbidden for the provided vehicle type
            if (weatherPhenomenonExtraFeeObj.getForbidden() && applicableVehicles.contains(vehicleTypeObj)) {
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
    
    
    private Float calculateATEF(VehicleType vehicleTypeObj, Float airTemperature) {
        logger.info("ENTERING - Calculate Air Temperature Extra Fee");
        
        // Fetch air temperature extra fee instance based on temperature and vehicle type - should contain only one instance
        List<AirTemperatureExtraFee> applicableFees = airTemperatureExtraFeeRepository.findByTemperatureAndVehicleType(airTemperature, vehicleTypeObj);
        logger.info("applicableFees: {}", applicableFees);

        // If no applicable fees are found, return 0.0f
        if (applicableFees.isEmpty()) {
            logger.info("No applicable extra fees found for given air temperature");
            logger.info("EXITING -- ATEF.isEmpty: {}", 0.0f);
            logger.info("-------------------------");
            return 0.0f;  // Return 0.0f if no fees are applicable
        }

        // Assign the instance to a variable for further handling
        AirTemperatureExtraFee airTemperatureExtraFeeObj = applicableFees.get(0);
        
        // Get the air temperature extra fee from the matching fee instance
        Float atef = airTemperatureExtraFeeObj.getExtraFee();
        logger.info("EXITING -- ATEF: {}", atef);
        logger.info("-------------------------");
        return atef;

    }

    private Float calculateWSEF(VehicleType vehicleTypeObj, Float windSpeed) throws Exception {
            logger.info("ENTERING - Calculate Wind Speed Extra Fee");
        
        // Fetch wind speed extra fee instance based on wind speed and vehicle type - should contain only one instance
        List<WindSpeedExtraFee> applicableFees = windSpeedExtraFeeRepository.findByWindSpeedAndVehicleType(windSpeed, vehicleTypeObj);
        logger.info("applicableFees: {}", applicableFees);

        if (applicableFees.isEmpty()) {
            logger.info("No applicable extra fees found for given wind speed");
            logger.info("EXITING -- WSEF.isEmpty: {}", 0.0f);
            logger.info("-------------------------");
            return 0.0f;  // Return 0.0f if no fees are applicable
        }
        
        // Assign the instance to a variable for further handling
        WindSpeedExtraFee windSpeedExtraFeeObj = applicableFees.get(0);

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
