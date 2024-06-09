package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    // Find latest weather data by WMO code
    @Query(value = "SELECT * FROM weather_data WHERE wmo_code = :wmoCode ORDER BY observation_timestamp DESC LIMIT 1", nativeQuery = true)
    WeatherData findLatestByWMOCode(@Param("wmoCode") int wmoCode);


}
