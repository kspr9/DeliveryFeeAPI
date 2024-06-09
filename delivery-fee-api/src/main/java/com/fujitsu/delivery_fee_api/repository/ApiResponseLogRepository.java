package com.fujitsu.delivery_fee_api.repository;

import com.fujitsu.delivery_fee_api.model.ApiResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiResponseLogRepository extends JpaRepository<ApiResponseLog, Long> {
    // Custom methods can be added here
}
