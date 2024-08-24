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
       /**
        * Retrieves the latest WeatherPhenomenonExtraFee record based on the given phenomenon category, vehicle type, and query time.
        *
        * @param  phenomenonCategory  the category of the weather phenomenon
        * @param  vehicleTypeId       the ID of the vehicle type
        * @param  queryTime           the query time to filter the records
        * @return                     the latest WeatherPhenomenonExtraFee record
        */
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

    /**
     * Retrieves a list of WeatherPhenomenonExtraFee records that overlap with the given phenomenon category and vehicle IDs.
     *
     * @param  phenomenonCategory  the category of the weather phenomenon
     * @param  vehicleIds          the IDs of the vehicles
     * @return                     a list of overlapping WeatherPhenomenonExtraFee records
     */
    @Query("SELECT wpf FROM WeatherPhenomenonExtraFee wpf " +
           "JOIN wpf.applicableVehicles v " +
           "WHERE wpf.isActive = true " +
           "AND wpf.phenomenonCategory = :phenomenonCategory " +
           "AND v.id IN :vehicleIds")
    List<WeatherPhenomenonExtraFee> findOverlappingFees(
            @Param("phenomenonCategory") WeatherPhenomenonCategory phenomenonCategory,
            @Param("vehicleIds") Set<Long> vehicleIds);
       
}