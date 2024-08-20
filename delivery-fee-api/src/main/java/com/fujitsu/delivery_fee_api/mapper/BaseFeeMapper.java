package com.fujitsu.delivery_fee_api.mapper;

import com.fujitsu.delivery_fee_api.dto.BaseFeeDTO;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseFeeMapper {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "vehicleType.id", target = "vehicleTypeId")
    @Mapping(source = "baseFee", target = "baseFee")
    BaseFeeDTO toDto(RegionalBaseFee regionalBaseFee);

    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "vehicleTypeId", target = "vehicleType.id")
    @Mapping(source = "baseFee", target = "baseFee")
    RegionalBaseFee toEntity(BaseFeeDTO baseFeeDto);

    List<BaseFeeDTO> toDtoList(List<RegionalBaseFee> regionalBaseFees);
}