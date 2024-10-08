package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.dto.AirTemperatureExtraFeeDTO;
import com.fujitsu.delivery_fee_api.dto.WindSpeedExtraFeeDTO;
import com.fujitsu.delivery_fee_api.dto.WeatherPhenomenonExtraFeeDTO;
import com.fujitsu.delivery_fee_api.exception.FeeExistsException;
import com.fujitsu.delivery_fee_api.exception.NotFoundException;
import com.fujitsu.delivery_fee_api.mapper.AirTemperatureExtraFeeMapper;
import com.fujitsu.delivery_fee_api.mapper.WindSpeedExtraFeeMapper;
import com.fujitsu.delivery_fee_api.mapper.WeatherPhenomenonExtraFeeMapper;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import com.fujitsu.delivery_fee_api.repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.WindSpeedExtraFeeRepository;
import com.fujitsu.delivery_fee_api.repository.WeatherPhenomenonExtraFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtraFeeService {

    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;
    private final AirTemperatureExtraFeeMapper airTemperatureExtraFeeMapper;
    private final WindSpeedExtraFeeMapper windSpeedExtraFeeMapper;
    private final WeatherPhenomenonExtraFeeMapper weatherPhenomenonExtraFeeMapper;

    /**
     * Creates a new Air Temperature Extra Fee.
     *
     * @param dto The DTO containing the Air Temperature Extra Fee details.
     * @return The created AirTemperatureExtraFeeDTO.
     * @throws FeeExistsException if an overlapping Air Temperature Extra Fee already exists.
     */
    @Transactional
    public AirTemperatureExtraFeeDTO createAirTemperatureExtraFee(AirTemperatureExtraFeeDTO dto) {
        checkForOverlappingAirTemperatureFee(dto);
        AirTemperatureExtraFee entity = airTemperatureExtraFeeMapper.toEntity(dto);
        entity.setEffectiveDate(LocalDateTime.now());
        entity.setIsActive(true);
        return airTemperatureExtraFeeMapper.toDto(airTemperatureExtraFeeRepository.save(entity));
    }

    /**
     * Retrieves all Air Temperature Extra Fees.
     *
     * @return A list of all AirTemperatureExtraFeeDTOs.
     */
    public List<AirTemperatureExtraFeeDTO> getAllAirTemperatureExtraFees() {
        return airTemperatureExtraFeeRepository.findAll().stream()
                .map(airTemperatureExtraFeeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing Air Temperature Extra Fee.
     *
     * @param id The ID of the Air Temperature Extra Fee to update.
     * @param dto The DTO containing the updated Air Temperature Extra Fee details.
     * @return The updated AirTemperatureExtraFeeDTO.
     * @throws NotFoundException if the Air Temperature Extra Fee with the given ID is not found.
     */
    @Transactional
    public AirTemperatureExtraFeeDTO updateAirTemperatureExtraFee(Long id, AirTemperatureExtraFeeDTO dto) {
        AirTemperatureExtraFee existingFee = airTemperatureExtraFeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Air Temperature Extra Fee not found"));
        
        // Update fields
        existingFee.setMinTemp(dto.getMinTemp());
        existingFee.setMaxTemp(dto.getMaxTemp());
        existingFee.setApplicableVehicles(airTemperatureExtraFeeMapper.idsToVehicles(dto.getApplicableVehicleIds()));
        existingFee.setExtraFee(dto.getExtraFee());
        existingFee.setIsActive(dto.getIsActive());

        return airTemperatureExtraFeeMapper.toDto(airTemperatureExtraFeeRepository.save(existingFee));
    }

    /**
     * Deletes an Air Temperature Extra Fee.
     *
     * @param id The ID of the Air Temperature Extra Fee to delete.
     * @throws NotFoundException if the Air Temperature Extra Fee with the given ID is not found.
     */
    @Transactional
    public void deleteAirTemperatureExtraFee(Long id) {
        if (!airTemperatureExtraFeeRepository.existsById(id)) {
            throw new NotFoundException("Air Temperature Extra Fee not found");
        }
        airTemperatureExtraFeeRepository.deleteById(id);
    }

    /**
     * Creates a new Wind Speed Extra Fee.
     *
     * @param dto The DTO containing the Wind Speed Extra Fee details.
     * @return The created WindSpeedExtraFeeDTO.
     * @throws FeeExistsException if an overlapping Wind Speed Extra Fee already exists.
     */
    @Transactional
    public WindSpeedExtraFeeDTO createWindSpeedExtraFee(WindSpeedExtraFeeDTO dto) {
        checkForOverlappingWindSpeedFee(dto);
        WindSpeedExtraFee entity = windSpeedExtraFeeMapper.toEntity(dto);
        entity.setEffectiveDate(LocalDateTime.now());
        entity.setIsActive(true);
        return windSpeedExtraFeeMapper.toDto(windSpeedExtraFeeRepository.save(entity));
    }

    /**
     * Retrieves all Wind Speed Extra Fees.
     *
     * @return A list of all WindSpeedExtraFeeDTOs.
     */
    public List<WindSpeedExtraFeeDTO> getAllWindSpeedExtraFees() {
        return windSpeedExtraFeeRepository.findAll().stream()
                .map(windSpeedExtraFeeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing Wind Speed Extra Fee.
     *
     * @param id The ID of the Wind Speed Extra Fee to update.
     * @param dto The DTO containing the updated Wind Speed Extra Fee details.
     * @return The updated WindSpeedExtraFeeDTO.
     * @throws NotFoundException if the Wind Speed Extra Fee with the given ID is not found.
     */
    @Transactional
    public WindSpeedExtraFeeDTO updateWindSpeedExtraFee(Long id, WindSpeedExtraFeeDTO dto) {
        WindSpeedExtraFee existingFee = windSpeedExtraFeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wind Speed Extra Fee not found"));
        
        // Update fields
        existingFee.setMinSpeed(dto.getMinSpeed());
        existingFee.setMaxSpeed(dto.getMaxSpeed());
        existingFee.setApplicableVehicles(windSpeedExtraFeeMapper.idsToVehicles(dto.getApplicableVehicleIds()));
        existingFee.setExtraFee(dto.getExtraFee());
        existingFee.setForbidden(dto.getForbidden());
        existingFee.setIsActive(dto.getIsActive());

        return windSpeedExtraFeeMapper.toDto(windSpeedExtraFeeRepository.save(existingFee));
    }

    /**
     * Deletes an Wind Speed Extra Fee.
     *
     * @param id The ID of the Wind Speed Extra Fee to delete.
     * @throws NotFoundException if the Wind Speed Extra Fee with the given ID is not found.
     */
    @Transactional
    public void deleteWindSpeedExtraFee(Long id) {
        if (!windSpeedExtraFeeRepository.existsById(id)) {
            throw new NotFoundException("Wind Speed Extra Fee not found");
        }
        windSpeedExtraFeeRepository.deleteById(id);
    }

    /**
     * Creates a new Weather Phenomenon Extra Fee.
     * 
     * @param dto The DTO containing the Weather Phenomenon Extra Fee details.
     * @return The created WeatherPhenomenonExtraFeeDTO.
     * @throws FeeExistsException if an overlapping Weather Phenomenon Extra Fee already exists.
     */
    @Transactional
    public WeatherPhenomenonExtraFeeDTO createWeatherPhenomenonExtraFee(WeatherPhenomenonExtraFeeDTO dto) {
        checkForOverlappingWeatherPhenomenonFee(dto);
        WeatherPhenomenonExtraFee entity = weatherPhenomenonExtraFeeMapper.toEntity(dto);
        entity.setEffectiveDate(LocalDateTime.now());
        entity.setIsActive(true);
        return weatherPhenomenonExtraFeeMapper.toDto(weatherPhenomenonExtraFeeRepository.save(entity));
    }

    /**
     * Retrieves all Weather Phenomenon Extra Fees.
     *
     * @return A list of all WeatherPhenomenonExtraFeeDTOs.
     */
    public List<WeatherPhenomenonExtraFeeDTO> getAllWeatherPhenomenonExtraFees() {
        return weatherPhenomenonExtraFeeRepository.findAll().stream()
                .map(weatherPhenomenonExtraFeeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing Weather Phenomenon Extra Fee.
     * 
     * @param id The ID of the Weather Phenomenon Extra Fee to update.
     * @param dto The DTO containing the updated Weather Phenomenon Extra Fee details.
     * @return The updated WeatherPhenomenonExtraFeeDTO.
     * @throws NotFoundException if the Weather Phenomenon Extra Fee with the given ID is not found.
     */
    @Transactional
    public WeatherPhenomenonExtraFeeDTO updateWeatherPhenomenonExtraFee(Long id, WeatherPhenomenonExtraFeeDTO dto) {
        WeatherPhenomenonExtraFee existingFee = weatherPhenomenonExtraFeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Weather Phenomenon Extra Fee not found"));
        
        // Update fields
        existingFee.setPhenomenonCategory(dto.getPhenomenonCategory());
        existingFee.setApplicableVehicles(weatherPhenomenonExtraFeeMapper.idsToVehicles(dto.getApplicableVehicleIds()));
        existingFee.setExtraFee(dto.getExtraFee());
        existingFee.setForbidden(dto.getForbidden());
        existingFee.setIsActive(dto.getIsActive());

        return weatherPhenomenonExtraFeeMapper.toDto(weatherPhenomenonExtraFeeRepository.save(existingFee));
    }

    /**
     * Deletes an Weather Phenomenon Extra Fee.
     * 
     * @param id The ID of the Weather Phenomenon Extra Fee to delete.
     * @throws NotFoundException if the Weather Phenomenon Extra Fee with the given ID is not found.
     */
    @Transactional
    public void deleteWeatherPhenomenonExtraFee(Long id) {
        if (!weatherPhenomenonExtraFeeRepository.existsById(id)) {
            throw new NotFoundException("Weather Phenomenon Extra Fee not found");
        }
        weatherPhenomenonExtraFeeRepository.deleteById(id);
    }

    // Helper methods
    private void checkForOverlappingAirTemperatureFee(AirTemperatureExtraFeeDTO dto) {
        List<AirTemperatureExtraFee> overlappingFees = airTemperatureExtraFeeRepository.findOverlappingFees(
                dto.getMinTemp(), dto.getMaxTemp(), dto.getApplicableVehicleIds());
        if (!overlappingFees.isEmpty()) {
            throw new FeeExistsException("An overlapping Air Temperature Extra Fee already exists");
        }
    }

    private void checkForOverlappingWindSpeedFee(WindSpeedExtraFeeDTO dto) {
        List<WindSpeedExtraFee> overlappingFees = windSpeedExtraFeeRepository.findOverlappingFees(
                dto.getMinSpeed(), dto.getMaxSpeed(), dto.getApplicableVehicleIds());
        if (!overlappingFees.isEmpty()) {
            throw new FeeExistsException("An overlapping Wind Speed Extra Fee already exists");
        }
    }

    private void checkForOverlappingWeatherPhenomenonFee(WeatherPhenomenonExtraFeeDTO dto) {
        List<WeatherPhenomenonExtraFee> overlappingFees = weatherPhenomenonExtraFeeRepository.findOverlappingFees(
                dto.getPhenomenonCategory(), dto.getApplicableVehicleIds());
        if (!overlappingFees.isEmpty()) {
            throw new FeeExistsException("An overlapping Weather Phenomenon Extra Fee already exists");
        }
    }
}