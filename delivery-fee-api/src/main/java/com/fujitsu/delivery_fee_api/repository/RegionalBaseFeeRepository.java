package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.City;
import com.fujitsu.delivery_fee_api.model.VehicleType;
import com.fujitsu.delivery_fee_api.model.fee_tables.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {
    @Query("SELECT r.baseFee FROM RegionalBaseFee r WHERE r.city = :city AND r.vehicleType = :vehicleType")
    Float fetchBaseFee(City city, VehicleType vehicleType);
}
