package com.fujitsu.delivery_fee_api.mapper;

import com.fujitsu.delivery_fee_api.dto.WindSpeedExtraFeeDTO;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WindSpeedExtraFeeMapper {

    @Mapping(target = "applicableVehicleIds", source = "applicableVehicles", qualifiedByName = "vehiclesToIds")
    WindSpeedExtraFeeDTO toDto(WindSpeedExtraFee windSpeedExtraFee);

    @Mapping(target = "applicableVehicles", source = "applicableVehicleIds", qualifiedByName = "idsToVehicles")
    WindSpeedExtraFee toEntity(WindSpeedExtraFeeDTO windSpeedExtraFeeDTO);

    @Named("vehiclesToIds")
    default Set<Long> vehiclesToIds(Set<VehicleType> vehicles) {
        if (vehicles == null) {
            return null;
        }
        return vehicles.stream()
                .map(VehicleType::getId)
                .collect(Collectors.toSet());
    }

    @Named("idsToVehicles")
    default Set<VehicleType> idsToVehicles(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream()
                .map(id -> {
                    VehicleType vehicleType = new VehicleType();
                    vehicleType.setId(id);
                    return vehicleType;
                })
                .collect(Collectors.toSet());
    }
}