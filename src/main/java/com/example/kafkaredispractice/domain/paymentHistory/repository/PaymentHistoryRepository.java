package com.example.kafkaredispractice.domain.paymentHistory.repository;

import com.example.kafkaredispractice.common.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
