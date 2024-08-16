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
    @Query("SELECT r FROM RegionalBaseFee r " +
           "WHERE r.city.id = :cityId AND r.vehicleType.id = :vehicleTypeId AND " +
           "r.isActive = true AND r.effectiveDate <= :queryTime " +
           "ORDER BY r.effectiveDate DESC")
    Optional<RegionalBaseFee> findLatestBaseFee(@Param("cityId") Long cityId, 
                                                @Param("vehicleTypeId") Long vehicleTypeId, 
                                                @Param("queryTime") LocalDateTime queryTime);

    Optional<RegionalBaseFee> findByCityAndVehicleType(City city, VehicleType vehicleType);
}