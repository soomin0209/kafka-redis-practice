package com.example.kafkaredispractice.domain.delivery.service;

import com.example.kafkaredispractice.common.entity.Delivery;
import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void createDeliveryFromPayment(PaymentCompletedEvent event) {
        Delivery delivery = Delivery.from(event);
        deliveryRepository.save(delivery);

        log.info("[Delivery DB] 배송 준비 생성 : orderId={}, status={}", delivery.getOrderId(), delivery.getStatus());
    }
}
