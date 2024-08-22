package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.dto.AirTemperatureExtraFeeDTO;
import com.fujitsu.delivery_fee_api.dto.WindSpeedExtraFeeDTO;
import com.fujitsu.delivery_fee_api.dto.WeatherPhenomenonExtraFeeDTO;
import com.fujitsu.delivery_fee_api.service.ExtraFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/fees/extra_fee")
@RequiredArgsConstructor
public class ExtraFeeController {

    private final ExtraFeeService extraFeeService;

    // Air Temperature Extra Fee endpoints
    @PostMapping("/air_temperature")
    public ResponseEntity<AirTemperatureExtraFeeDTO> createAirTemperatureExtraFee(@Valid @RequestBody AirTemperatureExtraFeeDTO dto) {
        AirTemperatureExtraFeeDTO createdFee = extraFeeService.createAirTemperatureExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    @GetMapping("/air_temperature")
    public ResponseEntity<List<AirTemperatureExtraFeeDTO>> getAllAirTemperatureExtraFees() {
        List<AirTemperatureExtraFeeDTO> fees = extraFeeService.getAllAirTemperatureExtraFees();
        return ResponseEntity.ok(fees);
    }

    @PutMapping("/air_temperature/{id}")
    public ResponseEntity<AirTemperatureExtraFeeDTO> updateAirTemperatureExtraFee(@PathVariable Long id, @Valid @RequestBody AirTemperatureExtraFeeDTO dto) {
        AirTemperatureExtraFeeDTO updatedFee = extraFeeService.updateAirTemperatureExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    @DeleteMapping("/air_temperature/{id}")
    public ResponseEntity<Void> deleteAirTemperatureExtraFee(@PathVariable Long id) {
        extraFeeService.deleteAirTemperatureExtraFee(id);
        return ResponseEntity.noContent().build();
    }

    // Wind Speed Extra Fee endpoints
    @PostMapping("/wind_speed")
    public ResponseEntity<WindSpeedExtraFeeDTO> createWindSpeedExtraFee(@Valid @RequestBody WindSpeedExtraFeeDTO dto) {
        WindSpeedExtraFeeDTO createdFee = extraFeeService.createWindSpeedExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    @GetMapping("/wind_speed")
    public ResponseEntity<List<WindSpeedExtraFeeDTO>> getAllWindSpeedExtraFees() {
        List<WindSpeedExtraFeeDTO> fees = extraFeeService.getAllWindSpeedExtraFees();
        return ResponseEntity.ok(fees);
    }

    @PutMapping("/wind_speed/{id}")
    public ResponseEntity<WindSpeedExtraFeeDTO> updateWindSpeedExtraFee(@PathVariable Long id, @Valid @RequestBody WindSpeedExtraFeeDTO dto) {
        WindSpeedExtraFeeDTO updatedFee = extraFeeService.updateWindSpeedExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    @DeleteMapping("/wind_speed/{id}")
    public ResponseEntity<Void> deleteWindSpeedExtraFee(@PathVariable Long id) {
        extraFeeService.deleteWindSpeedExtraFee(id);
        return ResponseEntity.noContent().build();
    }

    // Weather Phenomenon Extra Fee endpoints
    @PostMapping("/weather_phenomenon")
    public ResponseEntity<WeatherPhenomenonExtraFeeDTO> createWeatherPhenomenonExtraFee(@Valid @RequestBody WeatherPhenomenonExtraFeeDTO dto) {
        WeatherPhenomenonExtraFeeDTO createdFee = extraFeeService.createWeatherPhenomenonExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    @GetMapping("/weather_phenomenon")
    public ResponseEntity<List<WeatherPhenomenonExtraFeeDTO>> getAllWeatherPhenomenonExtraFees() {
        List<WeatherPhenomenonExtraFeeDTO> fees = extraFeeService.getAllWeatherPhenomenonExtraFees();
        return ResponseEntity.ok(fees);
    }

    @PutMapping("/weather_phenomenon/{id}")
    public ResponseEntity<WeatherPhenomenonExtraFeeDTO> updateWeatherPhenomenonExtraFee(@PathVariable Long id, @Valid @RequestBody WeatherPhenomenonExtraFeeDTO dto) {
        WeatherPhenomenonExtraFeeDTO updatedFee = extraFeeService.updateWeatherPhenomenonExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    @DeleteMapping("/weather_phenomenon/{id}")
    public ResponseEntity<Void> deleteWeatherPhenomenonExtraFee(@PathVariable Long id) {
        extraFeeService.deleteWeatherPhenomenonExtraFee(id);
        return ResponseEntity.noContent().build();
    }
}