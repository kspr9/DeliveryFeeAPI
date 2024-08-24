package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {
    /**
     * Retrieves the latest base fee for a given city and vehicle type, 
     * effective at or before the specified query time.
     *
     * @param cityId        the ID of the city
     * @param vehicleTypeId the ID of the vehicle type
     * @param queryTime     the query time
     * @return the latest base fee, or an empty Optional if none is found
     */
    @Query("SELECT r FROM RegionalBaseFee r " +
           "WHERE r.city.id = :cityId AND r.vehicleType.id = :vehicleTypeId AND " +
           "r.effectiveDate <= :queryTime " +
           "ORDER BY r.effectiveDate DESC LIMIT 1")
    Optional<RegionalBaseFee> findLatestBaseFee(@Param("cityId") Long cityId, 
                                                @Param("vehicleTypeId") Long vehicleTypeId, 
                                                @Param("queryTime") LocalDateTime queryTime);

    /**
     * Retrieves the current active base fee for a given city and vehicle type.
     *
     * @param cityId        the ID of the city
     * @param vehicleTypeId the ID of the vehicle type
     * @return the current active base fee, or an empty Optional if none is found
     */
    @Query("SELECT r FROM RegionalBaseFee r " +
           "WHERE r.city.id = :cityId AND r.vehicleType.id = :vehicleTypeId AND " +
           "r.isActive = true " +
           "ORDER BY r.effectiveDate DESC LIMIT 1")
    Optional<RegionalBaseFee> findCurrentActiveBaseFee(@Param("cityId") Long cityId, 
                                                       @Param("vehicleTypeId") Long vehicleTypeId);

    
    Optional<RegionalBaseFee> findByCityAndVehicleType(City city, VehicleType vehicleType);

    Optional<RegionalBaseFee> findByCityIdAndVehicleTypeId(Long cityId, Long vehicleId);
}