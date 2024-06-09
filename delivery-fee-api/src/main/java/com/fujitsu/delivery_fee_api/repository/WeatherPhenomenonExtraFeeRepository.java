package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.fee_tables.WeatherPhenomenonExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonExtraFeeRepository extends JpaRepository<WeatherPhenomenonExtraFee, Long> {
    // method for finding weather phenomenon extra fee by its name
    WeatherPhenomenonExtraFee findByPhenomenonCategoryCode(String phenomenonCategoryCode);
}
