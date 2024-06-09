package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirTemperatureExtraFeeRepository extends JpaRepository<AirTemperatureExtraFee, Long> {
    @Query(value = "SELECT atef.* FROM air_temperature_extra_fee atef " +
                   "INNER JOIN air_temperature_fee_vehicle atfv ON atef.id = atfv.fee_id " +
                   "WHERE (:temperature BETWEEN atef.min_temp AND atef.max_temp OR (:temperature <= atef.max_temp AND atef.min_temp IS NULL)) " +
                   "AND atef.is_active = true " +
                   "AND atef.effective_date <= :queryTime " +
                   "AND atfv.vehicle_type_id = :vehicleTypeId " +
                   "ORDER BY atef.effective_date DESC " +
                   "LIMIT 1",
          nativeQuery = true)
    AirTemperatureExtraFee findLatestByTemperatureAndVehicleTypeAndQueryTime(@Param("temperature") Float temperature, 
                                                                             @Param("vehicleTypeId") Long vehicleTypeId, 
                                                                             @Param("queryTime") LocalDateTime queryTime);
}


