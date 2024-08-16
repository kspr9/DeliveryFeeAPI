package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    VehicleType findByName(String name);
}
