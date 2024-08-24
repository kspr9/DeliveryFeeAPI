package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.AirTemperatureExtraFee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirTemperatureExtraFeeRepository extends JpaRepository<AirTemperatureExtraFee, Long> {
       /**
        * Retrieves the latest AirTemperatureExtraFee entity that matches the given temperature, vehicle type, and query time.
        *
        * @param temperature the temperature to search for
        * @param vehicleTypeId the ID of the vehicle type to search for
        * @param queryTime the query time to search for
        * @return the latest matching AirTemperatureExtraFee entity, or an empty Optional if no match is found
        */
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


       /**
        * Retrieves a list of AirTemperatureExtraFee entities that overlap with the given temperature range and vehicle IDs.
        *
        * @param minTemp the minimum temperature of the range to search for
        * @param maxTemp the maximum temperature of the range to search for
        * @param vehicleIds the IDs of the vehicles to search for
        * @return the list of overlapping AirTemperatureExtraFee entities
        */
       @Query("SELECT atef FROM AirTemperatureExtraFee atef " +
              "JOIN atef.applicableVehicles v " +
              "WHERE atef.isActive = true " +
              "AND ((atef.minTemp <= :maxTemp AND :minTemp <= atef.maxTemp) OR " +
              "     (atef.minTemp IS NULL AND :minTemp <= atef.maxTemp) OR " +
              "     (atef.maxTemp IS NULL AND :maxTemp >= atef.minTemp)) " +
              "AND v.id IN :vehicleIds")
       List<AirTemperatureExtraFee> findOverlappingFees(@Param("minTemp") Float minTemp,
                                                        @Param("maxTemp") Float maxTemp,
                                                        @Param("vehicleIds") Set<Long> vehicleIds);
                                                                                    
}