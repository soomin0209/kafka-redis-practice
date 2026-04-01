package com.example.kafkaredispractice.domain.delivery.repository;

import com.example.kafkaredispractice.common.entity.Delivery;
import com.example.kafkaredispractice.common.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findTop20ByUserIdOrderByStatusUpdatedAtDesc(Long userId);

    List<Delivery> findByStatusAndStatusUpdatedAtBefore(DeliveryStatus deliveryStatus, LocalDateTime threshold);
}
