package com.example.kafkaredispractice.domain.delivery.model.response;

import com.example.kafkaredispractice.common.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponse(
        Long deliveryId,
        Long orderId,
        Long productId,
        DeliveryStatus deliveryStatus,
        String statusUpdatedAt
) {}
