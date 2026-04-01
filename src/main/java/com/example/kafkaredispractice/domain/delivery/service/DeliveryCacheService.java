package com.example.kafkaredispractice.domain.delivery.service;

import com.example.kafkaredispractice.common.entity.Delivery;
import com.example.kafkaredispractice.common.enums.DeliveryStatus;
import com.example.kafkaredispractice.domain.delivery.model.response.DeliveryResponse;
import com.example.kafkaredispractice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCacheService {

    private final DeliveryRepository deliveryRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String DELIVERY_KEY = "delivery:status:";
    private static final String USER_DELIVERY_KEY = "user_deliveries:";

    public void cacheDelivery(Delivery delivery) {
        String key = DELIVERY_KEY + delivery.getId();

        stringRedisTemplate.opsForHash().putAll(key, Map.of(
                "status", delivery.getStatus().name(),
                "orderId", delivery.getOrderId().toString(),
                "productId", delivery.getProductId().toString(),
                "statusUpdatedAt", delivery.getStatusUpdatedAt().toString()
        ));

        String userDeliveryKey = USER_DELIVERY_KEY + delivery.getUserId();

        // LocalDateTime을 점수로 변환
        double score = delivery.getStatusUpdatedAt().toEpochSecond(ZoneOffset.UTC);

        stringRedisTemplate.opsForZSet().add(userDeliveryKey, delivery.getId().toString(), score);

        log.info("[Delivery-Redis] 캐시에 저장 완료 : key={}, status={}", key, delivery.getStatus());
    }

    public List<DeliveryResponse> findUserDeliveries(Long userId) {
        String userKey = USER_DELIVERY_KEY + userId;

        Set<String> deliveryIdList = stringRedisTemplate.opsForZSet().reverseRange(userKey, 0, 19);

        // 캐시에 없으면 DB 직접 조회
        if (deliveryIdList == null || deliveryIdList.isEmpty()) {
            return deliveryRepository.findTop20ByUserIdOrderByStatusUpdatedAtDesc(userId)
                    .stream()
                    .map(delivery -> new DeliveryResponse(
                            delivery.getId(),
                            delivery.getOrderId(),
                            delivery.getProductId(),
                            delivery.getStatus(),
                            delivery.getStatusUpdatedAt().toString()
                    ))
                    .toList();
        }

        List<DeliveryResponse> result = new ArrayList<>();
        for (String deliveryId : deliveryIdList) {
            String key = DELIVERY_KEY + deliveryId;
            Map<Object, Object> cached = stringRedisTemplate.opsForHash().entries(key);

            if (cached.isEmpty()) {
                Delivery delivery = deliveryRepository.findById(Long.valueOf(deliveryId)).orElseThrow();
                result.add(new DeliveryResponse(
                        delivery.getId(),
                        delivery.getOrderId(),
                        delivery.getProductId(),
                        delivery.getStatus(),
                        delivery.getStatusUpdatedAt().toString()
                ));
                continue;
            }

            DeliveryResponse response = new DeliveryResponse(
                    Long.valueOf(deliveryId),
                    Long.valueOf(cached.get("orderId").toString()),
                    Long.valueOf(cached.get("productId").toString()),
                    DeliveryStatus.valueOf(cached.get("status").toString()),
                    cached.get("statusUpdatedAt").toString()
            );

            result.add(response);
        }

        return result;
    }
}
