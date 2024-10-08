
package com.fujitsu.delivery_fee_api.config;

import com.fujitsu.delivery_fee_api.model.*;
import com.fujitsu.delivery_fee_api.model.fee_tables.*;
import com.fujitsu.delivery_fee_api.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;

    @Override
    public void run(String... args) throws Exception {
    
        // Add test data for City
        City tallinn = new City("Tallinn", 26038);
        City tartu = new City("Tartu", 26242);
        City parnu = new City("Pärnu", 41803);
        City haapsalu = new City("Haapsalu", 26123);
        cityRepository.save(tallinn);
        cityRepository.save(tartu);
        cityRepository.save(parnu);
        cityRepository.save(haapsalu);

        // Add test data for VehicleType
        VehicleType car = new VehicleType("Car", false);
        VehicleType scooter = new VehicleType("Scooter", true);
        VehicleType bike = new VehicleType("Bike", true);
        vehicleTypeRepository.save(car);
        vehicleTypeRepository.save(scooter);
        vehicleTypeRepository.save(bike);
        

        List<WeatherPhenomenonType> weatherPhenomenonTypes = Arrays.asList(
            WeatherPhenomenonType.createNoneType("Clear"),
            WeatherPhenomenonType.createNoneType("Few clouds"),
            WeatherPhenomenonType.createNoneType("Variable clouds"),
            WeatherPhenomenonType.createNoneType("Cloudy with clear spells"),
            WeatherPhenomenonType.createNoneType("Overcast"),
            new WeatherPhenomenonType("Light snow shower", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Moderate snow shower", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Heavy snow shower", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Light shower", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Moderate shower", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Heavy shower", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Light rain", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Moderate rain", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Heavy rain", WeatherPhenomenonCategory.RAIN),
            new WeatherPhenomenonType("Glaze", WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL),
            new WeatherPhenomenonType("Light sleet", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Moderate sleet", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Light snowfall", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Moderate snowfall", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Heavy snowfall", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Blowing snow", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Drifting snow", WeatherPhenomenonCategory.SNOW_OR_SLEET),
            new WeatherPhenomenonType("Hail", WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL),
            WeatherPhenomenonType.createNoneType("Mist"),
            WeatherPhenomenonType.createNoneType("Fog"),
            new WeatherPhenomenonType("Thunder", WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL),
            new WeatherPhenomenonType("Thunderstorm", WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL)
        );

        weatherPhenomenonTypeRepository.saveAll(weatherPhenomenonTypes);
        

        // Add test data for WeatherData
        //

        // January - Heavy snow shower, very cold
        // Tests ATEF for Scooter/Bike, WPEF for Scooter/Bike (Snow or Sleet)
        WeatherData weatherDataTallinnJanuary = new WeatherData("Tallinn-Harku", 26038, -15.0f, 5.0f, "Heavy snow shower", 1673328000); // January 10, 2023
        weatherDataRepository.save(weatherDataTallinnJanuary);

        // February - Moderate snowfall, cold
        // Tests ATEF for Scooter/Bike, WPEF for Scooter/Bike (Snow or Sleet)
        WeatherData weatherDataTartuFebruary = new WeatherData("Tartu-Tõravere", 26242, -12.0f, 8.0f, "Moderate snowfall", 1676006400); // February 10, 2023
        weatherDataRepository.save(weatherDataTartuFebruary);

        // March - Light shower, chilly
        // Tests ATEF for Scooter/Bike, WPEF for Scooter/Bike (Rain)
        WeatherData weatherDataParnuMarch = new WeatherData("Pärnu", 41803, -2.0f, 6.0f, "Light shower", 1678502400); // March 10, 2023
        weatherDataRepository.save(weatherDataParnuMarch);

        // April - Light rain, mild
        // Tests WPEF for Scooter/Bike (Rain)
        WeatherData weatherDataTallinnApril = new WeatherData("Tallinn-Harku", 26038, 8.0f, 7.0f, "Light rain", 1681180800); // April 10, 2023
        weatherDataRepository.save(weatherDataTallinnApril);

        // May - Clear skies, warm
        // Tests scenario with no additional fees
        WeatherData weatherDataTartuMay = new WeatherData("Tartu-Tõravere", 26242, 15.0f, 3.0f, "Clear", 1683859200); // May 10, 2023
        weatherDataRepository.save(weatherDataTartuMay);

        // June - Few clouds, summer warmth
        // Tests scenario with no additional fees
        WeatherData weatherDataParnuJune = new WeatherData("Pärnu", 41803, 18.0f, 12.0f, "Few clouds", 1686537600); // June 10, 2023
        weatherDataRepository.save(weatherDataParnuJune);

        // July - Thunderstorms, hot
        // Tests WPEF restriction for Scooter/Bike (Thunder, Glaze, or Hail)
        WeatherData weatherDataTallinnJuly = new WeatherData("Tallinn-Harku", 26038, 25.0f, 0.5f, "Thunderstorm", 1689216000); // July 10, 2023
        weatherDataRepository.save(weatherDataTallinnJuly);

        // August - Heavy rain, moderately warm
        // Tests WPEF for Scooter/Bike (Rain)
        WeatherData weatherDataTartuAugust = new WeatherData("Tartu-Tõravere", 26242, 20.0f, 12.0f, "Heavy rain", 1691894400); // August 10, 2023
        weatherDataRepository.save(weatherDataTartuAugust);

        // September - Variable clouds, moderate winds
        // Tests WSEF for Bike (Wind speed 10-20 m/s)
        WeatherData weatherDataParnuSeptember = new WeatherData("Pärnu", 41803, 10.0f, 15.0f, "Variable clouds", 1694572800); // September 10, 2023
        weatherDataRepository.save(weatherDataParnuSeptember);

        // October - Foggy, cool
        // Tests scenarios with no additional fees
        WeatherData weatherDataTallinnOctober = new WeatherData("Tallinn-Harku", 26038, 5.0f, 1.0f, "Fog", 1697251200); // October 10, 2023
        weatherDataRepository.save(weatherDataTallinnOctober);

        // November - Moderate sleet, early winter
        // Tests WPEF for Scooter/Bike (Snow or Sleet)
        WeatherData weatherDataTartuNovember = new WeatherData("Tartu-Tõravere", 26242, -1.0f, 4.0f, "Moderate sleet", 1699843200); // November 10, 2023
        weatherDataRepository.save(weatherDataTartuNovember);

        // December - Blowing snow, cold, high winds
        // Tests ATEF for Scooter/Bike, WSEF restriction for Bike (Wind > 20 m/s), WPEF for Scooter/Bike (Snow or Sleet)
        WeatherData weatherDataParnuDecember = new WeatherData("Pärnu", 41803, -5.0f, 22.0f, "Blowing snow", 1702521600); // December 14, 2023
        weatherDataRepository.save(weatherDataParnuDecember);

        // Additional Samples for 2023
        // Thunder only (July)
        // Tests WPEF restriction for Scooter/Bike (Thunder, Glaze, or Hail)
        WeatherData weatherDataTartuJulyThunder = new WeatherData("Tartu-Tõravere", 26242, 23.0f, 3.0f, "Thunder", 1690675200); // July 20, 2023
        weatherDataRepository.save(weatherDataTartuJulyThunder);

        // Hail (April)
        // Tests WPEF restriction for Scooter/Bike (Thunder, Glaze, or Hail)
        WeatherData weatherDataTallinnAprilHail = new WeatherData("Tallinn-Harku", 26038, 6.0f, 2.0f, "Hail", 1683648000); // April 20, 2023
        weatherDataRepository.save(weatherDataTallinnAprilHail);

        // Glaze (November)
        // Tests WPEF restriction for Scooter/Bike (Thunder, Glaze, or Hail), 
        WeatherData weatherDataParnuNovemberGlaze = new WeatherData("Pärnu", 41803, 0.0f, 7.0f, "Glaze", 1701715200); // November 20, 2023
        weatherDataRepository.save(weatherDataParnuNovemberGlaze);

        // High wind speed but other parameters allow scooter fees (March)
        // Tests WSEF restriction for Bike (Wind > 20 m/s), ATEF for Scooter (Temperature between -10°C and 0°C)
        WeatherData weatherDataTallinnMarchHighWind = new WeatherData("Tallinn-Harku", 26038, -3.0f, 25.0f, "Moderate snow shower", 1680921600); // March 20, 2023
        weatherDataRepository.save(weatherDataTallinnMarchHighWind);

        // Another high wind scenario (October)
        // Tests WSEF restriction for Bike (Wind > 20 m/s)
        WeatherData weatherDataTartuOctoberHighWind = new WeatherData("Tartu-Tõravere", 26242, 7.0f, 23.0f, "Light rain", 1699344000); // October 20, 2023
        weatherDataRepository.save(weatherDataTartuOctoberHighWind);




        
        //          BUSINESS RULES          //
        //
        // Add business rule entries for RegionalBaseFee
        RegionalBaseFee rbfTallinnCar = new RegionalBaseFee(tallinn, car, new BigDecimal("4.0"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfTallinnScooter = new RegionalBaseFee(tallinn, scooter, new BigDecimal("3.5"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfTallinnBike = new RegionalBaseFee(tallinn, bike, new BigDecimal("3.0"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        regionalBaseFeeRepository.save(rbfTallinnCar);
        regionalBaseFeeRepository.save(rbfTallinnScooter);
        regionalBaseFeeRepository.save(rbfTallinnBike);

        RegionalBaseFee rbfTartuCar = new RegionalBaseFee(tartu, car, new BigDecimal("3.5"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfTartuScooter = new RegionalBaseFee(tartu, scooter, new BigDecimal("3.0"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfTartuBike = new RegionalBaseFee(tartu, bike, new BigDecimal("2.5"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        regionalBaseFeeRepository.save(rbfTartuCar);
        regionalBaseFeeRepository.save(rbfTartuScooter);
        regionalBaseFeeRepository.save(rbfTartuBike);

        RegionalBaseFee rbfParnuCar = new RegionalBaseFee(parnu, car, new BigDecimal("3.0"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfParnuScooter = new RegionalBaseFee(parnu, scooter, new BigDecimal("2.5"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        RegionalBaseFee rbfParnuBike = new RegionalBaseFee(parnu, bike, new BigDecimal("2.0"), LocalDateTime.of(2023, 1, 1, 0, 0), true);
        regionalBaseFeeRepository.save(rbfParnuCar);
        regionalBaseFeeRepository.save(rbfParnuScooter);
        regionalBaseFeeRepository.save(rbfParnuBike);

        // Add business rule entries for WeatherPhenomenonExtraFee table
        //
        Set<VehicleType> wpefVechicleSet = new HashSet<>(Arrays.asList(scooter, bike));

        WeatherPhenomenonExtraFee wpefSnowOrSleet = new WeatherPhenomenonExtraFee(
            WeatherPhenomenonCategory.SNOW_OR_SLEET, 
            wpefVechicleSet, 
            new BigDecimal("1.0"), 
            false, 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        weatherPhenomenonExtraFeeRepository.save(wpefSnowOrSleet);
        
        WeatherPhenomenonExtraFee wpefRain = new WeatherPhenomenonExtraFee(
            WeatherPhenomenonCategory.RAIN, 
            wpefVechicleSet, 
            new BigDecimal("0.5"), 
            false, 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        weatherPhenomenonExtraFeeRepository.save(wpefRain);

        WeatherPhenomenonExtraFee wpefThunderGlazeHail = new WeatherPhenomenonExtraFee(
            WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL, 
            wpefVechicleSet, 
            null, 
            true, 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        weatherPhenomenonExtraFeeRepository.save(wpefThunderGlazeHail);

        // Add business rule entries for AirTemperatureExtraFee table
        // 
        Set<VehicleType> atefVechicleSet = new HashSet<>(Arrays.asList(scooter, bike));

        // if AT is below -10, then the fee is 1.0 for applicable vehicles
        AirTemperatureExtraFee atefBelowM10 = new AirTemperatureExtraFee(
            null, 
            -10.0f, 
            atefVechicleSet, 
            new BigDecimal("1.0"), 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        airTemperatureExtraFeeRepository.save(atefBelowM10);

        // if AT is between -10 and 0 deg, then the fee is 0.5 for applicable vehicles
        AirTemperatureExtraFee atefBetween10A0 = new AirTemperatureExtraFee(
            -10.0f, 
            0.0f, 
            atefVechicleSet, 
            new BigDecimal("0.5"), 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        airTemperatureExtraFeeRepository.save(atefBetween10A0);

        // Add business rule entries for WindSpeedExtraFee table
        Set<VehicleType> wsVechicleSet = new HashSet<>(Arrays.asList(bike));

        // if WS is between 10 and 20 m/s, then the fee is 0.5 for applicable vehicles
        WindSpeedExtraFee wsefBetween10A20 = new WindSpeedExtraFee(
            10.0f, 
            20.0f, 
            wsVechicleSet,
            new BigDecimal("0.5"), 
            false, 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        windSpeedExtraFeeRepository.save(wsefBetween10A20);

        // if WS is above 20 m/s, then forbidden is true for applicable vehicles
        WindSpeedExtraFee wsefAbove20 = new WindSpeedExtraFee(
            20.0f, 
            null, 
            wsVechicleSet,
            null, 
            true, 
            LocalDateTime.of(2023, 1, 1, 0, 0), 
            true
        );
        windSpeedExtraFeeRepository.save(wsefAbove20);
            
    }
}
