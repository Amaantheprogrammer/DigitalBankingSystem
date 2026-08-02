package com.MyProject.DigitalBankingSystem.fraud.repository;

import com.MyProject.DigitalBankingSystem.fraud.entity.FraudLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudLogRepository extends JpaRepository<FraudLog, Long> {
    Page<FraudLog> findAllByOrderByDetectedAtDesc(Pageable pageable);
}
