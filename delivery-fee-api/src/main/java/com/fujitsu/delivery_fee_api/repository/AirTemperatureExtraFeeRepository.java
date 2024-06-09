package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirTemperatureExtraFeeRepository extends JpaRepository<AirTemperatureExtraFee, Long> {
    @Query("SELECT atef FROM AirTemperatureExtraFee atef " +
            "JOIN atef.applicableVehicles av " +
            "WHERE (:temperature BETWEEN atef.minTemp AND atef.maxTemp) OR (:temperature <= atef.maxTemp AND atef.minTemp IS NULL) " +
            "AND atef.isActive = true " +
            "AND atef.effectiveDate <= CURRENT_DATE() " +
            "AND av = :vehicleType")
    List<AirTemperatureExtraFee> findByTemperatureAndVehicleType(@Param("temperature") Float temperature, @Param("vehicleType") VehicleType vehicleType);

}
