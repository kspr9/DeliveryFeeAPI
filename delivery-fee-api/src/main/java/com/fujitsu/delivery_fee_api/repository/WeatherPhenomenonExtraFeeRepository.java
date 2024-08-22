package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonCategory;
import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonExtraFeeRepository extends JpaRepository<WeatherPhenomenonExtraFee, Long> {    
       @Query("SELECT wpf FROM WeatherPhenomenonExtraFee wpf " +
              "JOIN wpf.applicableVehicles v " +
              "WHERE wpf.phenomenonCategory = :phenomenonCategory " +
              "AND wpf.isActive = true " +
              "AND wpf.effectiveDate <= :queryTime " +
              "AND v.id = :vehicleTypeId " +
              "ORDER BY wpf.effectiveDate DESC")
       WeatherPhenomenonExtraFee findLatestByPhenomenonCategoryVehicleTypeAndQueryTime(
              @Param("phenomenonCategory") WeatherPhenomenonCategory phenomenonCategory,
              @Param("vehicleTypeId") Long vehicleTypeId,
              @Param("queryTime") LocalDateTime queryTime);

    @Query("SELECT wpf FROM WeatherPhenomenonExtraFee wpf " +
           "JOIN wpf.applicableVehicles v " +
           "WHERE wpf.isActive = true " +
           "AND wpf.phenomenonCategory = :phenomenonCategory " +
           "AND v.id IN :vehicleIds")
    List<WeatherPhenomenonExtraFee> findOverlappingFees(
            @Param("phenomenonCategory") WeatherPhenomenonCategory phenomenonCategory,
            @Param("vehicleIds") Set<Long> vehicleIds);
       
}