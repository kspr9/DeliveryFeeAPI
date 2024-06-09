package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.WeatherPhenomenonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonTypeRepository extends JpaRepository<WeatherPhenomenonType, Long> {
    WeatherPhenomenonType findByPhenomenon(String phenomenon);
}
