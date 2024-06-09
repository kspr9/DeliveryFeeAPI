package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonExtraFeeRepository extends JpaRepository<WeatherPhenomenonExtraFee, Long> {
    @Query(value = "SELECT wpf.* FROM weather_phenomenon_extra_fee wpf " +
                   "INNER JOIN weather_phenomenon_fee_vehicle wpfv ON wpf.id = wpfv.fee_id " +
                   "WHERE wpf.phenomenon_category_code = :phenomenonCategoryCode " +
                   "AND wpf.is_active = true " +
                   "AND wpf.effective_date <= :queryTime " +
                   "AND wpfv.vehicle_type_id = :vehicleTypeId " +
                   "ORDER BY wpf.effective_date DESC " +
                   "LIMIT 1",
          nativeQuery = true)
    WeatherPhenomenonExtraFee findLatestByPhenomenonCategoryCodeVehicleTypeAndQueryTime(@Param("phenomenonCategoryCode") String phenomenonCategoryCode,
                                                                                       @Param("vehicleTypeId") Long vehicleTypeId,
                                                                                       @Param("queryTime") LocalDateTime queryTime);
}


