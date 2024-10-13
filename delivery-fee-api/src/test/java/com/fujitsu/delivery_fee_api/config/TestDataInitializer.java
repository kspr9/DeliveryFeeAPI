package com.fujitsu.delivery_fee_api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonCategory;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.RegionalBaseFeeRepository;
import com.fujitsu.delivery_fee_api.repository.VehicleTypeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonTypeRepository;
import com.fujitsu.delivery_fee_api.repository.WindSpeedExtraFeeRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class TestDataInitializer {
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;

    @PostConstruct
    public void initialize() {
        
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

        ////////////////////////////////////////////////////////////
        //      Categorize weather phenomena
        ////////////////////////////////////////////////////////////
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

        //////////////////////////////////////
        //          BUSINESS RULES          //
        //////////////////////////////////////
        
        LocalDateTime effectiveDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        regionalBaseFeeRepository.saveAll(Arrays.asList(
            new RegionalBaseFee(tallinn, car, new BigDecimal("4.0"), effectiveDate, true),
            new RegionalBaseFee(tallinn, scooter, new BigDecimal("3.5"), effectiveDate, true),
            new RegionalBaseFee(tallinn, bike, new BigDecimal("3.0"), effectiveDate, true),
            new RegionalBaseFee(tartu, car, new BigDecimal("3.5"), effectiveDate, true),
            new RegionalBaseFee(tartu, scooter, new BigDecimal("3.0"), effectiveDate, true),
            new RegionalBaseFee(tartu, bike, new BigDecimal("2.5"), effectiveDate, true),
            new RegionalBaseFee(parnu, car, new BigDecimal("3.0"), effectiveDate, true),
            new RegionalBaseFee(parnu, scooter, new BigDecimal("2.5"), effectiveDate, true),
            new RegionalBaseFee(parnu, bike, new BigDecimal("2.0"), effectiveDate, true)
        ));

        ///////////////////////////////////////////////////////////////
        //  Add business rule entries for WeatherPhenomenonExtraFee  //
        ///////////////////////////////////////////////////////////////
        Set<VehicleType> applicableVehicles = new HashSet<>(Arrays.asList(scooter, bike));

        weatherPhenomenonExtraFeeRepository.saveAll(Arrays.asList(
            new WeatherPhenomenonExtraFee(WeatherPhenomenonCategory.SNOW_OR_SLEET, applicableVehicles, new BigDecimal("1.0"), false, effectiveDate, true),
            new WeatherPhenomenonExtraFee(WeatherPhenomenonCategory.RAIN, applicableVehicles, new BigDecimal("0.5"), false, effectiveDate, true),
            new WeatherPhenomenonExtraFee(WeatherPhenomenonCategory.THUNDER_GLAZE_OR_HAIL, applicableVehicles, null, true, effectiveDate, true)
        ));

        ////////////////////////////////////////////////////////////
        //  Add business rule entries for AirTemperatureExtraFee  //
        ////////////////////////////////////////////////////////////
        applicableVehicles = new HashSet<>(Arrays.asList(scooter, bike));

        airTemperatureExtraFeeRepository.saveAll(Arrays.asList(
            new AirTemperatureExtraFee(null, -10.0f, applicableVehicles, new BigDecimal("1.0"), effectiveDate, true),
            new AirTemperatureExtraFee(-10.0f, 0.0f, applicableVehicles, new BigDecimal("0.5"), effectiveDate, true)
        ));

        ///////////////////////////////////////////////////////
        //  Add business rule entries for WindSpeedExtraFee  //
        ///////////////////////////////////////////////////////
        applicableVehicles = new HashSet<>(Arrays.asList(bike));

        windSpeedExtraFeeRepository.saveAll(Arrays.asList(
            new WindSpeedExtraFee(10.0f, 20.0f, applicableVehicles, new BigDecimal("0.5"), false, effectiveDate, true),
            new WindSpeedExtraFee(20.0f, null, applicableVehicles, null, true, effectiveDate, true)
        ));
    }
}
