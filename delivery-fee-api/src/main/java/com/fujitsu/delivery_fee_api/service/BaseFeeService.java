package com.fujitsu.delivery_fee_api.service;

import com.fujitsu.delivery_fee_api.dto.BaseFeeDTO;
import com.fujitsu.delivery_fee_api.exception.NotFoundException;
import com.fujitsu.delivery_fee_api.mapper.BaseFeeMapper;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;
import com.fujitsu.delivery_fee_api.repository.CityRepository;
import com.fujitsu.delivery_fee_api.repository.RegionalBaseFeeRepository;
import com.fujitsu.delivery_fee_api.repository.VehicleTypeRepository;

import lombok.RequiredArgsConstructor;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseFeeService {

    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final CityRepository cityRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final BaseFeeMapper baseFeeMapper = Mappers.getMapper(BaseFeeMapper.class);

    public List<BaseFeeDTO> getAllBaseFees() {
        List<RegionalBaseFee> baseFees = regionalBaseFeeRepository.findAll();
        return baseFeeMapper.toDtoList(baseFees);
    }

    public void createBaseFee(BaseFeeDTO baseFeeDto) {
        Long cityId = baseFeeDto.getCityId();
        Long vehicleTypeId = baseFeeDto.getVehicleTypeId();
        checkIfExists(cityId, vehicleTypeId);
        
        Optional<RegionalBaseFee> existingActiveFee = regionalBaseFeeRepository.findCurrentActiveBaseFee(cityId, vehicleTypeId);
        
        if (existingActiveFee.isPresent()) {
            RegionalBaseFee currentFee = existingActiveFee.get();
            currentFee.setIsActive(false);
            regionalBaseFeeRepository.save(currentFee);
        }

        RegionalBaseFee newBaseFee = baseFeeMapper.toEntity(baseFeeDto);
        newBaseFee.setEffectiveDate(LocalDateTime.now());
        newBaseFee.setIsActive(true);
        regionalBaseFeeRepository.save(newBaseFee);

    }

    public void updateBaseFee(Long id, BaseFeeDTO baseFeeDto) {
        RegionalBaseFee existingBaseFee = regionalBaseFeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No BaseFee entry with that ID."));

        existingBaseFee.setBaseFee(baseFeeDto.getBaseFee());

        regionalBaseFeeRepository.save(existingBaseFee);
    }

    public void deleteBaseFee(Long id) {
        if (!regionalBaseFeeRepository.existsById(id)) {
            throw new NotFoundException("No BaseFee entry with that ID.");
        } else {
            regionalBaseFeeRepository.deleteById(id);
        }
    }

    private void checkIfExists(Long cityId, Long vehicleTypeId) {
        if (!cityRepository.existsById(cityId)) {
            throw new NotFoundException("No City entry with that ID.");
        }
        if (!vehicleTypeRepository.existsById(vehicleTypeId)) {
            throw new NotFoundException("No VehicleType entry with that ID.");
        }
    }
}