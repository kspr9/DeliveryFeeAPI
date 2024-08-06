package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.WeatherData;

import java.util.List;


//import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    // Find latest weather data by WMO code
    @Query(value = "SELECT * FROM weather_data WHERE wmo_code = :wmoCode ORDER BY observation_timestamp DESC LIMIT 1", nativeQuery = true)
    WeatherData findLatestByWMOCode(@Param("wmoCode") int wmoCode);

    // Find latest weather data by WMO code and dateTime
    @Query(value = "SELECT * FROM weather_data WHERE wmo_code = :wmoCode AND observation_timestamp <= :dateTime ORDER BY observation_timestamp DESC LIMIT 1", nativeQuery = true)
    WeatherData findLatestByWMOCodeAsOf(@Param("wmoCode") int wmoCode, @Param("dateTime") int dateTime);

    @Query("SELECT w FROM WeatherData w WHERE w.wmoCode = :wmoCode AND w.observationTimestamp <= :timestamp ORDER BY w.observationTimestamp DESC")
    List<WeatherData> findWeatherDataByWmoCodeAndTimestamp(@Param("wmoCode") Integer wmoCode, @Param("timestamp") Integer timestamp);
}
