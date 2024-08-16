package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonExtraFeeRepository extends JpaRepository<WeatherPhenomenonExtraFee, Long> {
    @Query("SELECT wpf FROM WeatherPhenomenonExtraFee wpf " +
           "JOIN wpf.applicableVehicles v " +
           "WHERE wpf.phenomenonCategoryName = :phenomenonCategoryName " +
           "AND wpf.isActive = true " +
           "AND wpf.effectiveDate <= :queryTime " +
           "AND v.id = :vehicleTypeId " +
           "ORDER BY wpf.effectiveDate DESC")
    WeatherPhenomenonExtraFee findLatestByPhenomenonCategoryNameVehicleTypeAndQueryTime(@Param("phenomenonCategoryName") String phenomenonCategoryName,
                                                                                        @Param("vehicleTypeId") Long vehicleTypeId,
                                                                                        @Param("queryTime") LocalDateTime queryTime);
}