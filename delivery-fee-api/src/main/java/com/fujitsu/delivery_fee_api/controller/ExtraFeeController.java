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

    /**
     * Creates a new Air Temperature Extra Fee.
     *
     * @param dto The DTO containing the Air Temperature Extra Fee details.
     * @return ResponseEntity containing the created AirTemperatureExtraFeeDTO and HTTP status 201 (CREATED).
     */
    @PostMapping("/air_temperature")
    public ResponseEntity<AirTemperatureExtraFeeDTO> createAirTemperatureExtraFee(@Valid @RequestBody AirTemperatureExtraFeeDTO dto) {
        AirTemperatureExtraFeeDTO createdFee = extraFeeService.createAirTemperatureExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    /**
     * Retrieves all Air Temperature Extra Fees.
     *
     * @return ResponseEntity containing a list of all AirTemperatureExtraFeeDTOs and HTTP status 200 (OK).
     */
    @GetMapping("/air_temperature")
    public ResponseEntity<List<AirTemperatureExtraFeeDTO>> getAllAirTemperatureExtraFees() {
        List<AirTemperatureExtraFeeDTO> fees = extraFeeService.getAllAirTemperatureExtraFees();
        return ResponseEntity.ok(fees);
    }

    /**
     * Updates an existing Air Temperature Extra Fee.
     *
     * @param id The ID of the Air Temperature Extra Fee to update.
     * @param dto The DTO containing the updated Air Temperature Extra Fee details.
     * @return ResponseEntity containing the updated AirTemperatureExtraFeeDTO and HTTP status 200 (OK).
     */
    @PutMapping("/air_temperature/{id}")
    public ResponseEntity<AirTemperatureExtraFeeDTO> updateAirTemperatureExtraFee(@PathVariable Long id, @Valid @RequestBody AirTemperatureExtraFeeDTO dto) {
        AirTemperatureExtraFeeDTO updatedFee = extraFeeService.updateAirTemperatureExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    /**
     * Deletes an Air Temperature Extra Fee.
     *
     * @param id The ID of the Air Temperature Extra Fee to delete.
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT).
     */
    @DeleteMapping("/air_temperature/{id}")
    public ResponseEntity<Void> deleteAirTemperatureExtraFee(@PathVariable Long id) {
        extraFeeService.deleteAirTemperatureExtraFee(id);
        return ResponseEntity.noContent().build();
    }

    // Wind Speed Extra Fee endpoints

    /**
     * Creates a new Wind Speed Extra Fee.
     *
     * @param dto The DTO containing the Wind Speed Extra Fee details.
     * @return ResponseEntity containing the created WindSpeedExtraFeeDTO and HTTP status 201 (CREATED).
     */
    @PostMapping("/wind_speed")
    public ResponseEntity<WindSpeedExtraFeeDTO> createWindSpeedExtraFee(@Valid @RequestBody WindSpeedExtraFeeDTO dto) {
        WindSpeedExtraFeeDTO createdFee = extraFeeService.createWindSpeedExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    /**
     * Retrieves all Wind Speed Extra Fees.
     *
     * @return ResponseEntity containing a list of all WindSpeedExtraFeeDTOs and HTTP status 200 (OK).
     */
    @GetMapping("/wind_speed")
    public ResponseEntity<List<WindSpeedExtraFeeDTO>> getAllWindSpeedExtraFees() {
        List<WindSpeedExtraFeeDTO> fees = extraFeeService.getAllWindSpeedExtraFees();
        return ResponseEntity.ok(fees);
    }

    /**
     * Updates an existing Wind Speed Extra Fee.
     *
     * @param id The ID of the Wind Speed Extra Fee to update.
     * @param dto The DTO containing the updated Wind Speed Extra Fee details.
     * @return ResponseEntity containing the updated WindSpeedExtraFeeDTO and HTTP status 200 (OK).
     */
    @PutMapping("/wind_speed/{id}")
    public ResponseEntity<WindSpeedExtraFeeDTO> updateWindSpeedExtraFee(@PathVariable Long id, @Valid @RequestBody WindSpeedExtraFeeDTO dto) {
        WindSpeedExtraFeeDTO updatedFee = extraFeeService.updateWindSpeedExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    /**
     * Deletes a Wind Speed Extra Fee.
     *
     * @param id The ID of the Wind Speed Extra Fee to delete.
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT).
     */
    @DeleteMapping("/wind_speed/{id}")
    public ResponseEntity<Void> deleteWindSpeedExtraFee(@PathVariable Long id) {
        extraFeeService.deleteWindSpeedExtraFee(id);
        return ResponseEntity.noContent().build();
    }

    // Weather Phenomenon Extra Fee endpoints

    /**
     * Creates a new Weather Phenomenon Extra Fee.
     *
     * @param dto The DTO containing the Weather Phenomenon Extra Fee details.
     * @return ResponseEntity containing the created WeatherPhenomenonExtraFeeDTO and HTTP status 201 (CREATED).
     */
    @PostMapping("/weather_phenomenon")
    public ResponseEntity<WeatherPhenomenonExtraFeeDTO> createWeatherPhenomenonExtraFee(@Valid @RequestBody WeatherPhenomenonExtraFeeDTO dto) {
        WeatherPhenomenonExtraFeeDTO createdFee = extraFeeService.createWeatherPhenomenonExtraFee(dto);
        return new ResponseEntity<>(createdFee, HttpStatus.CREATED);
    }

    /**
     * Retrieves all Weather Phenomenon Extra Fees.
     *
     * @return ResponseEntity containing a list of all WeatherPhenomenonExtraFeeDTOs and HTTP status 200 (OK).
     */
    @GetMapping("/weather_phenomenon")
    public ResponseEntity<List<WeatherPhenomenonExtraFeeDTO>> getAllWeatherPhenomenonExtraFees() {
        List<WeatherPhenomenonExtraFeeDTO> fees = extraFeeService.getAllWeatherPhenomenonExtraFees();
        return ResponseEntity.ok(fees);
    }

    /**
     * Updates an existing Weather Phenomenon Extra Fee.
     *
     * @param id The ID of the Weather Phenomenon Extra Fee to update.
     * @param dto The DTO containing the updated Weather Phenomenon Extra Fee details.
     * @return ResponseEntity containing the updated WeatherPhenomenonExtraFeeDTO and HTTP status 200 (OK).
     */
    @PutMapping("/weather_phenomenon/{id}")
    public ResponseEntity<WeatherPhenomenonExtraFeeDTO> updateWeatherPhenomenonExtraFee(@PathVariable Long id, @Valid @RequestBody WeatherPhenomenonExtraFeeDTO dto) {
        WeatherPhenomenonExtraFeeDTO updatedFee = extraFeeService.updateWeatherPhenomenonExtraFee(id, dto);
        return ResponseEntity.ok(updatedFee);
    }

    /**
     * Deletes a Weather Phenomenon Extra Fee.
     *
     * @param id The ID of the Weather Phenomenon Extra Fee to delete.
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT).
     */
    @DeleteMapping("/weather_phenomenon/{id}")
    public ResponseEntity<Void> deleteWeatherPhenomenonExtraFee(@PathVariable Long id) {
        extraFeeService.deleteWeatherPhenomenonExtraFee(id);
        return ResponseEntity.noContent().build();
    }
}