package com.fujitsu.delivery_fee_api.mapper;

import com.fujitsu.delivery_fee_api.dto.WeatherPhenomenonExtraFeeDTO;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WeatherPhenomenonExtraFeeMapper {

    @Mapping(target = "applicableVehicleIds", source = "applicableVehicles", qualifiedByName = "vehiclesToIds")
    WeatherPhenomenonExtraFeeDTO toDto(WeatherPhenomenonExtraFee weatherPhenomenonExtraFee);

    @Mapping(target = "applicableVehicles", source = "applicableVehicleIds", qualifiedByName = "idsToVehicles")
    WeatherPhenomenonExtraFee toEntity(WeatherPhenomenonExtraFeeDTO weatherPhenomenonExtraFeeDTO);

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