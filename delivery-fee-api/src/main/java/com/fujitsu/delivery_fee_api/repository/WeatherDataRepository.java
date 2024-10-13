package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.WeatherData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    @Query("SELECT w FROM WeatherData w WHERE w.wmoCode = :wmoCode ORDER BY w.observationTimestamp DESC LIMIT 1")
    WeatherData findLatestByWMOCode(@Param("wmoCode") int wmoCode);

    @Query("SELECT w FROM WeatherData w WHERE w.wmoCode = :wmoCode AND w.observationTimestamp <= :dateTime ORDER BY w.observationTimestamp DESC LIMIT 1")
    WeatherData findLatestByWMOCodeAsOf(@Param("wmoCode") int wmoCode, @Param("dateTime") int dateTime);

    @Query("SELECT w FROM WeatherData w WHERE w.wmoCode = :wmoCode AND w.observationTimestamp <= :dateTime ORDER BY w.observationTimestamp DESC LIMIT 1")
    Optional<WeatherData> findLatestByWMOCodeAsOfOpt(@Param("wmoCode") int wmoCode, @Param("dateTime") int dateTime);

    @Query("SELECT w FROM WeatherData w WHERE w.wmoCode = :wmoCode AND w.observationTimestamp <= :timestamp ORDER BY w.observationTimestamp DESC")
    List<WeatherData> findWeatherDataByWmoCodeAndTimestamp(@Param("wmoCode") Integer wmoCode, @Param("timestamp") Integer timestamp);
}