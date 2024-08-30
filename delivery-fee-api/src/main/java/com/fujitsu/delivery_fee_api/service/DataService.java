package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.VehicleTypeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonTypeRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataService {

    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;

    
    /**
     * Retrieves all cities from the database.
     *
     * @return a list of all cities
     */

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }


    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public Optional<City> updateCity(Long id, City cityDetails) {
        return cityRepository.findById(id)
                .map(city -> {
                    city.setName(cityDetails.getName());
                    city.setWmoCode(cityDetails.getWmoCode());
                    return cityRepository.save(city);
                });
    }

    public boolean deleteCity(Long id) {
        return cityRepository.findById(id)
                .map(city -> {
                    cityRepository.delete(city);
                    return true;
                })
                .orElse(false);
    }

    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }

    public Optional<VehicleType> getVehicleTypeById(Long id) {
        return vehicleTypeRepository.findById(id);
    }

    public VehicleType createVehicleType(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    public Optional<VehicleType> updateVehicleType(Long id, VehicleType vehicleTypeDetails) {
        return vehicleTypeRepository.findById(id)
                .map(vehicleType -> {
                    vehicleType.setName(vehicleTypeDetails.getName());
                    vehicleType.setExtraFeeApplicable(vehicleTypeDetails.getExtraFeeApplicable());
                    return vehicleTypeRepository.save(vehicleType);
                });
    }

    public boolean deleteVehicleType(Long id) {
        return vehicleTypeRepository.findById(id)
                .map(vehicleType -> {
                    vehicleTypeRepository.delete(vehicleType);
                    return true;
                })
                .orElse(false);
    }

    public List<WeatherPhenomenonType> getAllWeatherPhenomenonTypes() {
        return weatherPhenomenonTypeRepository.findAll();
    }

    public Optional<WeatherPhenomenonType> getWeatherPhenomenonTypeById(Long id) {
        return weatherPhenomenonTypeRepository.findById(id);
    }

    public WeatherPhenomenonType createWeatherPhenomenonType(WeatherPhenomenonType weatherPhenomenonType) {
        return weatherPhenomenonTypeRepository.save(weatherPhenomenonType);
    }

    public Optional<WeatherPhenomenonType> updateWeatherPhenomenonType(Long id, WeatherPhenomenonType weatherPhenomenonTypeDetails) {
        return weatherPhenomenonTypeRepository.findById(id)
                .map(weatherPhenomenonType -> {
                    weatherPhenomenonType.setPhenomenon(weatherPhenomenonTypeDetails.getPhenomenon());
                    weatherPhenomenonType.setCategory(weatherPhenomenonTypeDetails.getCategory());
                    return weatherPhenomenonTypeRepository.save(weatherPhenomenonType);
                });
    }

    public boolean deleteWeatherPhenomenonType(Long id) {
        return weatherPhenomenonTypeRepository.findById(id)
                .map(weatherPhenomenonType -> {
                    weatherPhenomenonTypeRepository.delete(weatherPhenomenonType);
                    return true;
                })
                .orElse(false);
    }
}