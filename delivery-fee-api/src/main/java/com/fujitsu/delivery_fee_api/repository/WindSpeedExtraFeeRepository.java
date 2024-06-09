package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WindSpeedExtraFeeRepository extends JpaRepository<WindSpeedExtraFee, Long> {
    @Query("SELECT wsef FROM WindSpeedExtraFee wsef " +
           "JOIN wsef.applicableVehicles av " +
           "WHERE (:windSpeed BETWEEN wsef.minSpeed AND wsef.maxSpeed) OR (:windSpeed >= wsef.minSpeed AND wsef.maxSpeed IS NULL) " +
           "AND wsef.isActive = true " +
           "AND wsef.effectiveDate <= CURRENT_DATE() " +
           "AND av = :vehicleType")
    List<WindSpeedExtraFee> findByWindSpeedAndVehicleType(@Param("windSpeed") Float windSpeed, @Param("vehicleType") VehicleType vehicleType);
}
