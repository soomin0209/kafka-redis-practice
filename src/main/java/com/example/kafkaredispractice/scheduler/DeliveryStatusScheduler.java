package com.example.kafkaredispractice.scheduler;

import com.example.kafkaredispractice.common.entity.Delivery;
import com.example.kafkaredispractice.common.enums.DeliveryStatus;
import com.example.kafkaredispractice.domain.delivery.repository.DeliveryRepository;
import com.example.kafkaredispractice.domain.delivery.service.DeliveryCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryStatusScheduler {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryCacheService deliveryCacheService;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void updatePreparingToShipping() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusSeconds(10);

        List<Delivery> preparingList = deliveryRepository.findByStatusAndStatusUpdatedAtBefore(DeliveryStatus.PREPARING, threshold);

        if (preparingList.isEmpty()) {
            return;
        }

        log.info("[Scheduler] PREPARING to SHIPPING 전환 대상 : {}건", preparingList.size());

        for (Delivery delivery : preparingList) {
            delivery.markShipping(now);
            deliveryRepository.save(delivery);
            deliveryCacheService.cacheDelivery(delivery);
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void updateShippingToCompleted() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusSeconds(10);

        List<Delivery> shippingList = deliveryRepository.findByStatusAndStatusUpdatedAtBefore(DeliveryStatus.SHIPPING, threshold);

        if (shippingList.isEmpty()) {
            return;
        }

        log.info("[Scheduler] SHIPPING to COMPLETED 전환 대상 : {}건", shippingList.size());

        for (Delivery delivery : shippingList) {
            delivery.markCompleted(now);
            deliveryRepository.save(delivery);
            deliveryCacheService.cacheDelivery(delivery);
        }
    }
}
