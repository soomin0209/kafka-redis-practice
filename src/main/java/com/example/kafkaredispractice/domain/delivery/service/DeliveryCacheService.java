package com.example.kafkaredispractice.domain.delivery.service;

import com.example.kafkaredispractice.common.entity.Delivery;
import com.example.kafkaredispractice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCacheService {

    private final DeliveryRepository deliveryRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public void cacheDelivery(Delivery delivery) {
        String key = "delivery:status:" + delivery.getId();

        stringRedisTemplate.opsForHash().putAll(key, Map.of(
                "status", delivery.getStatus().name(),
                "orderId", delivery.getOrderId().toString(),
                "productId", delivery.getProductId().toString(),
                "statusUpdateAt", delivery.getStatusUpdatedAt().toString()
        ));

        String userDeliveryKey = "user_delivery:" + delivery.getUserId();

        // LocalDateTime을 점수로 변환
        double score = delivery.getStatusUpdatedAt().toEpochSecond(ZoneOffset.UTC);

        stringRedisTemplate.opsForZSet().add(userDeliveryKey, delivery.getId().toString(), score);

        log.info("[Delivery-Redis] 캐시에 저장 완료 : key={}, status={}", key, delivery.getStatus());
    }
}
