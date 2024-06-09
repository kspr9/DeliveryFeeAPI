package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WindSpeedExtraFeeRepository extends JpaRepository<WindSpeedExtraFee, Long> {
    @Query(value = "SELECT wsef.* FROM wind_speed_extra_fee wsef " +
                   "INNER JOIN wind_speed_fee_vehicle wsfv ON wsef.id = wsfv.fee_id " +
                   "WHERE (:windSpeed BETWEEN wsef.min_speed AND wsef.max_speed OR (:windSpeed >= wsef.min_speed AND wsef.max_speed IS NULL)) " +
                   "AND wsef.is_active = true " +
                   "AND wsef.effective_date <= :queryTime " +
                   "AND wsfv.vehicle_type_id = :vehicleTypeId " +
                   "ORDER BY wsef.effective_date DESC " +
                   "LIMIT 1",
          nativeQuery = true)
    WindSpeedExtraFee findLatestByWindSpeedAndVehicleTypeAndQueryTime(@Param("windSpeed") Float windSpeed, 
                                                                      @Param("vehicleTypeId") Long vehicleTypeId, 
                                                                      @Param("queryTime") LocalDateTime queryTime);
}

