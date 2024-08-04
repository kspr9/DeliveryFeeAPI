package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {
    @Query(value = "SELECT r.base_fee FROM regional_base_fee r " +
                   "WHERE r.city_id = :cityId AND r.vehicle_type_id = :vehicleTypeId AND " +
                   "r.is_active = true AND r.effective_date <= :queryTime " +
                   "ORDER BY r.effective_date DESC " +
                   "LIMIT 1",
          nativeQuery = true)
    BigDecimal fetchLatestBaseFee(@Param("cityId") Long cityId, 
                             @Param("vehicleTypeId") Long vehicleTypeId, 
                             @Param("queryTime") LocalDateTime queryTime);
}

