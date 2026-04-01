package com.example.kafkaredispractice.common.model.kafka.event;

import com.example.kafkaredispractice.common.enums.Category;

public record PaymentCompletedEvent(
        Long paymentId,
        Long orderId,
        Long productId,
        Long userId,
        Category category,
        int quantity,
        String paidAt
) {}
