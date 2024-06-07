package com.fujitsu.delivery_fee_api.config;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private RegionalBaseFeeRepository regionalBaseFeeRepository;

    @Autowired
    private AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;

    @Autowired
    private WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;

    @Autowired
    private WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Add test data for City
            City tallinn = new City("Tallinn", 26038);
            City tartu = new City("Tartu", 26242);
            City parnu = new City("Pärnu", 41803);
            cityRepository.save(tallinn);
            cityRepository.save(tartu);
            cityRepository.save(parnu);

            // Add test data for VehicleType
            VehicleType car = new VehicleType("Car", false);
            VehicleType scooter = new VehicleType("Scooter", true);
            VehicleType bike = new VehicleType("Bike", true);
            vehicleTypeRepository.save(car);
            vehicleTypeRepository.save(scooter);
            vehicleTypeRepository.save(bike);
            
            
            // Add test data for the weather phemenon - needs to have the extrafee classes inserted
            WeatherPhenomenonType clear = new WeatherPhenomenonType("Clear", null);
            WeatherPhenomenonType fewClouds = new WeatherPhenomenonType("Few clouds", null);
            WeatherPhenomenonType variableClouds = new WeatherPhenomenonType("Variable clouds", null);
            WeatherPhenomenonType cloudyWithClearSpells = new WeatherPhenomenonType("Cloudy with clear spells", null);
            WeatherPhenomenonType overcast = new WeatherPhenomenonType("Overcast", null);
            WeatherPhenomenonType lightSnowShower = new WeatherPhenomenonType("Light snow shower", "SNOW OR SLEET");
            WeatherPhenomenonType moderateSnowShower = new WeatherPhenomenonType("Moderate snow shower", "SNOW OR SLEET");
            WeatherPhenomenonType heavySnowShower = new WeatherPhenomenonType("Heavy snow shower", "SNOW OR SLEET");
            WeatherPhenomenonType lightShower = new WeatherPhenomenonType("Light shower", "RAIN");
            WeatherPhenomenonType moderateShower = new WeatherPhenomenonType("Moderate shower", "RAIN");
            WeatherPhenomenonType heavyShower = new WeatherPhenomenonType("Heavy shower", "RAIN");
            WeatherPhenomenonType lightRain = new WeatherPhenomenonType("Light rain", "RAIN");
            WeatherPhenomenonType moderateRain = new WeatherPhenomenonType("Moderate rain", "RAIN");
            WeatherPhenomenonType heavyRain = new WeatherPhenomenonType("Heavy rain", "RAIN");
            WeatherPhenomenonType glaze = new WeatherPhenomenonType("Glaze", "THUNDER, GLAZE OR HAIL");
            WeatherPhenomenonType lightSleet = new WeatherPhenomenonType("Light sleet", "SNOW OR SLEET");
            WeatherPhenomenonType moderateSleet = new WeatherPhenomenonType("Moderate sleet", "SNOW OR SLEET");
            WeatherPhenomenonType lightSnowfall = new WeatherPhenomenonType("Light snowfall", "SNOW OR SLEET");
            WeatherPhenomenonType moderateSnowfall = new WeatherPhenomenonType("Moderate snowfall", "SNOW OR SLEET");
            WeatherPhenomenonType heavySnowfall = new WeatherPhenomenonType("Heavy snowfall", "SNOW OR SLEET");
            WeatherPhenomenonType blowingSnow = new WeatherPhenomenonType("Blowing snow", "SNOW OR SLEET");
            WeatherPhenomenonType driftingSnow = new WeatherPhenomenonType("Drifting snow", "SNOW OR SLEET");
            WeatherPhenomenonType hail = new WeatherPhenomenonType("Hail", "THUNDER, GLAZE OR HAIL");
            WeatherPhenomenonType mist = new WeatherPhenomenonType("Mist", null);
            WeatherPhenomenonType fog = new WeatherPhenomenonType("Fog", null);
            WeatherPhenomenonType thunder = new WeatherPhenomenonType("Thunder", "THUNDER, GLAZE OR HAIL");
            WeatherPhenomenonType thunderstorm = new WeatherPhenomenonType("Thunderstorm", "THUNDER, GLAZE OR HAIL");
            
            // Save instances to the repository
            weatherPhenomenonTypeRepository.save(clear);
            weatherPhenomenonTypeRepository.save(fewClouds);
            weatherPhenomenonTypeRepository.save(variableClouds);
            weatherPhenomenonTypeRepository.save(cloudyWithClearSpells);
            weatherPhenomenonTypeRepository.save(overcast);
            weatherPhenomenonTypeRepository.save(lightSnowShower);
            weatherPhenomenonTypeRepository.save(moderateSnowShower);
            weatherPhenomenonTypeRepository.save(heavySnowShower);
            weatherPhenomenonTypeRepository.save(lightShower);
            weatherPhenomenonTypeRepository.save(moderateShower);
            weatherPhenomenonTypeRepository.save(heavyShower);
            weatherPhenomenonTypeRepository.save(lightRain);
            weatherPhenomenonTypeRepository.save(moderateRain);
            weatherPhenomenonTypeRepository.save(heavyRain);
            weatherPhenomenonTypeRepository.save(glaze);
            weatherPhenomenonTypeRepository.save(lightSleet);
            weatherPhenomenonTypeRepository.save(moderateSleet);
            weatherPhenomenonTypeRepository.save(lightSnowfall);
            weatherPhenomenonTypeRepository.save(moderateSnowfall);
            weatherPhenomenonTypeRepository.save(heavySnowfall);
            weatherPhenomenonTypeRepository.save(blowingSnow);
            weatherPhenomenonTypeRepository.save(driftingSnow);
            weatherPhenomenonTypeRepository.save(hail);
            weatherPhenomenonTypeRepository.save(mist);
            weatherPhenomenonTypeRepository.save(fog);
            weatherPhenomenonTypeRepository.save(thunder);
            weatherPhenomenonTypeRepository.save(thunderstorm);
            

            // Add test data for WeatherData
            WeatherData weatherDataTallinn = new WeatherData("Tallinn-Harku", 26038, 21.7f, 3.5f, "Clear", 1717495442);
            weatherDataRepository.save(weatherDataTallinn);
            
            WeatherData weatherDataTartu = new WeatherData("Tartu-Tõravere", 26242, -2.1f, 4.7f, "Light snow shower", 1717495442);
            weatherDataRepository.save(weatherDataTartu);

            WeatherData weatherDataParnu = new WeatherData("Pärnu", 41803, 15.0f, 2.0f, "Rain", 1717495442);
            weatherDataRepository.save(weatherDataParnu);


            
            

            // Add business rule entries for RegionalBaseFee
            RegionalBaseFee rbfTallinnCar = new RegionalBaseFee(tallinn, car, 4.0f, LocalDateTime.now(), true);
            RegionalBaseFee rbfTallinnScooter = new RegionalBaseFee(tallinn, scooter, 3.5f, LocalDateTime.now(), true);
            RegionalBaseFee rbfTallinnBike = new RegionalBaseFee(tallinn, bike, 3.0f, LocalDateTime.now(), true);
            regionalBaseFeeRepository.save(rbfTallinnCar);
            regionalBaseFeeRepository.save(rbfTallinnScooter);
            regionalBaseFeeRepository.save(rbfTallinnBike);

            RegionalBaseFee rbfTartuCar = new RegionalBaseFee(tartu, car, 3.5f, LocalDateTime.now(), true);
            RegionalBaseFee rbfTartuScooter = new RegionalBaseFee(tartu, scooter, 3.0f, LocalDateTime.now(), true);
            RegionalBaseFee rbfTartuBike = new RegionalBaseFee(tartu, bike, 2.5f, LocalDateTime.now(), true);
            regionalBaseFeeRepository.save(rbfTartuCar);
            regionalBaseFeeRepository.save(rbfTartuScooter);
            regionalBaseFeeRepository.save(rbfTartuBike);

            RegionalBaseFee rbfParnuCar = new RegionalBaseFee(parnu, car, 3.0f, LocalDateTime.now(), true);
            RegionalBaseFee rbfParnuScooter = new RegionalBaseFee(parnu, scooter, 2.5f, LocalDateTime.now(), true);
            RegionalBaseFee rbfParnuBike = new RegionalBaseFee(parnu, bike, 2.0f, LocalDateTime.now(), true);
            regionalBaseFeeRepository.save(rbfParnuCar);
            regionalBaseFeeRepository.save(rbfParnuScooter);
            regionalBaseFeeRepository.save(rbfParnuBike);
            
            
            // Add business rule entries for WeatherPhenomenonExtraFee table
            //
            Set<VehicleType> wpefVechicleSet = new HashSet<>(Arrays.asList(scooter, bike));

            WeatherPhenomenonExtraFee wpefSnowOrSleet = new WeatherPhenomenonExtraFee("SNOW OR SLEET", wpefVechicleSet, 1.0f, false, LocalDateTime.now(), true);
            weatherPhenomenonExtraFeeRepository.save(wpefSnowOrSleet);
            
            WeatherPhenomenonExtraFee wpefRain = new WeatherPhenomenonExtraFee("RAIN", wpefVechicleSet, 0.5f, false, LocalDateTime.now(), true);
            weatherPhenomenonExtraFeeRepository.save(wpefRain);

            WeatherPhenomenonExtraFee wpefThunderGlazeHail = new WeatherPhenomenonExtraFee("THUNDER, GLAZE OR HAIL", wpefVechicleSet, null, true, LocalDateTime.now(), true);
            weatherPhenomenonExtraFeeRepository.save(wpefThunderGlazeHail);

            
            // Add business rule entries for AirTemperatureExtraFee table
            // 
            Set<VehicleType> atefVechicleSet = new HashSet<>(Arrays.asList(scooter, bike));
            
            // if AT is below -10, then the fee is 1.0 for applicable vehicles
            AirTemperatureExtraFee atefBelowM10 = new AirTemperatureExtraFee(null, -10.0f, atefVechicleSet, 1.0f, LocalDateTime.now(), true);
            airTemperatureExtraFeeRepository.save(atefBelowM10);

            // if AT is between -10 and 0 deg, then the fee is 0.5 for applicable vehicles
            AirTemperatureExtraFee atefBetween10A0 = new AirTemperatureExtraFee(-10.0f, 0.0f, atefVechicleSet, 0.5f, LocalDateTime.now(), true);
            airTemperatureExtraFeeRepository.save(atefBetween10A0);



            // Add business rule entries for WindSpeedExtraFee table
            // 
            Set<VehicleType> wsVechicleSet = new HashSet<>(Arrays.asList(bike));

            // if WS is between 10 and 20 m/s, then the fee is 0.5 for applicable vehicles
            WindSpeedExtraFee wsefBetween10A20 = new WindSpeedExtraFee(10.0f, 20.0f, 0.5f, false, LocalDateTime.now(), true, wsVechicleSet);
            windSpeedExtraFeeRepository.save(wsefBetween10A20);

            // if WS is above 20 m/s, then forbidden is true for applicable vehicles
            WindSpeedExtraFee wsefAbove20 = new WindSpeedExtraFee(20.0f, null, 0.5f, true, LocalDateTime.now(), true, wsVechicleSet);
            windSpeedExtraFeeRepository.save(wsefAbove20);
            
        };
    }
}
