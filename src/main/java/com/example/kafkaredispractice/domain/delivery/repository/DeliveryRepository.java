package com.example.kafkaredispractice.domain.delivery.repository;

import com.example.kafkaredispractice.common.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
