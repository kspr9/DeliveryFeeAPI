package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirTemperatureExtraFeeRepository extends JpaRepository<AirTemperatureExtraFee, Long> {
    @Query("SELECT atef FROM AirTemperatureExtraFee atef " +
           "JOIN atef.applicableVehicles v " +
           "WHERE (:temperature BETWEEN atef.minTemp AND atef.maxTemp OR (:temperature <= atef.maxTemp AND atef.minTemp IS NULL)) " +
           "AND atef.isActive = true " +
           "AND atef.effectiveDate <= :queryTime " +
           "AND v.id = :vehicleTypeId " +
           "ORDER BY atef.effectiveDate DESC")
    Optional<AirTemperatureExtraFee> findLatestByTemperatureAndVehicleTypeAndQueryTime(@Param("temperature") Float temperature, 
                                                                                       @Param("vehicleTypeId") Long vehicleTypeId, 
                                                                                       @Param("queryTime") LocalDateTime queryTime);
}