package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.WindSpeedExtraFee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WindSpeedExtraFeeRepository extends JpaRepository<WindSpeedExtraFee, Long> {
       @Query("SELECT wsef FROM WindSpeedExtraFee wsef " +
              "JOIN wsef.applicableVehicles v " +
              "WHERE (:windSpeed BETWEEN wsef.minSpeed AND wsef.maxSpeed OR (:windSpeed >= wsef.minSpeed AND wsef.maxSpeed IS NULL)) " +
              "AND wsef.isActive = true " +
              "AND wsef.effectiveDate <= :queryTime " +
              "AND v.id = :vehicleTypeId " +
              "ORDER BY wsef.effectiveDate DESC")
       WindSpeedExtraFee findLatestByWindSpeedAndVehicleTypeAndQueryTime(@Param("windSpeed") Float windSpeed, 
                                                                      @Param("vehicleTypeId") Long vehicleTypeId, 
                                                                      @Param("queryTime") LocalDateTime queryTime);

       @Query("SELECT wsef FROM WindSpeedExtraFee wsef " +
              "JOIN wsef.applicableVehicles v " +
              "WHERE wsef.isActive = true " +
              "AND ((wsef.minSpeed <= :maxSpeed AND :minSpeed <= wsef.maxSpeed) OR " +
              "     (wsef.minSpeed IS NULL AND :minSpeed <= wsef.maxSpeed) OR " +
              "     (wsef.maxSpeed IS NULL AND :maxSpeed >= wsef.minSpeed)) " +
              "AND v.id IN :vehicleIds")
       List<WindSpeedExtraFee> findOverlappingFees(@Param("minSpeed") Float minSpeed,
                                                 @Param("maxSpeed") Float maxSpeed,
                                                 @Param("vehicleIds") Set<Long> vehicleIds);
}