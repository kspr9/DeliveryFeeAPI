package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherData;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info("City Object: {}", cityObj);

        // Fetch VehicleType object based on supplied vehicle type
        VehicleType vehicleTypeObj = vehicleTypeRepository.findByVehicleType(vehicleType);
        logger.info("Vehicle Type Object: {}", vehicleTypeObj);
        
        // Fetch weather data corresponding to the weatherstation as per WMO code from the City object 
        WeatherData latestWeatherInCityObj = weatherDataRepository.findLatestByWMOCode(cityObj.getWmoCode());
        logger.info("Weather Data Object: {}", latestWeatherInCityObj);

        // Fetch the name of weather phenomenon in the latest weather data
        String weatherPhenomenonName = latestWeatherInCityObj.getWeatherPhenomenon();
        logger.info("Weather Phenomenon Name: {}", weatherPhenomenonName);
        
        // HANDLE WEATHER PHENOMENON EXTRA FEE
        // 
        // WP Extra Fees are only applicable if 
        // there is a weather phenomenon in the latest weather data
        // and the vehicle type belongs to extraFeeApplicable category
        if (weatherPhenomenonName != null && vehicleTypeObj.getExtraFeeApplicable() == true) {
            logger.info("Handling Weather Phenomenon Extra Fee");
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
            logger.info("Handling Air Temperature Extra Fee");
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
            logger.info("Handling Wind Speed Extra Fee");
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
        logger.info("Before final return");
        logger.info("Base Fee: {}", baseFee);
        logger.info("WPEF: {}", wpef);
        logger.info("ATEF: {}", atef);
        logger.info("WSEF: {}", wsef);
        totalFee += baseFee + atef + wsef + wpef;

        return totalFee;
    }

    private Float fetchBaseFee(City cityObj, VehicleType vehicleTypeObj) {
        // Fetch and calculate the base fee based on city and vehicle type
        Float rbf = regionalBaseFeeRepository.fetchBaseFee(cityObj, vehicleTypeObj);
        return rbf;
    }
    
    private Float calculateWPEF(String weatherPhenomenonName, VehicleType vehicleTypeObj) throws Exception {
        
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
                logger.info("Weather Phenomenon Extra Fee is forbidden for this vehicle type: {}", vehicleTypeObj);
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
    
            // Get the weather phenomenon extra fee from the matching fee instance
            wpef += weatherPhenomenonExtraFeeObj.getExtraFee();
        }

        logger.info("WPEF: {}", wpef);
        return wpef;
    }
    
    
    private Float calculateATEF(VehicleType vehicleTypeObj, Float airTemperature) {
        
        // Fetch air temperature extra fee instance based on temperature and vehicle type - should contain only one instance
        List<AirTemperatureExtraFee> applicableFees = airTemperatureExtraFeeRepository.findByTemperatureAndVehicleType(airTemperature, vehicleTypeObj);

        // If no applicable fees are found, return 0.0f
        if (applicableFees.isEmpty()) {
            return 0.0f;  // Return 0.0f if no fees are applicable
        }

        // Assign the instance to a variable for further handling
        AirTemperatureExtraFee airTemperatureExtraFeeObj = applicableFees.get(0);

        // Get the air temperature extra fee from the matching fee instance
        Float atef = airTemperatureExtraFeeObj.getExtraFee();
        logger.info("ATEF: {}", atef);
        return atef;

    }

    private Float calculateWSEF(VehicleType vehicleTypeObj, Float windSpeed) throws Exception {
        
        // Fetch wind speed extra fee instance based on wind speed and vehicle type - should contain only one instance
        List<WindSpeedExtraFee> applicableFees = windSpeedExtraFeeRepository.findByWindSpeedAndVehicleType(windSpeed, vehicleTypeObj);

        if (applicableFees.isEmpty()) {
            return 0.0f;  // Return 0.0f if no fees are applicable
        }
        
        // Assign the instance to a variable for further handling
        WindSpeedExtraFee windSpeedExtraFeeObj = applicableFees.get(0);

        // Throw an exception if the wind speed extra fee category is forbidden for the provided vehicle type
        if (windSpeedExtraFeeObj.getForbidden()) {
            throw new Exception("Usage of selected vehicle type is forbidden");
        }
        
        // Get the wind speed extra fee from the matching fee instance
        Float wsef = windSpeedExtraFeeObj.getExtraFee();
        logger.info("WSEF: {}", wsef);
        return wsef; 
    }
    

}
