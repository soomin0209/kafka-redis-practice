package com.example.kafkaredispractice.domain.payment.model.request;

import com.example.kafkaredispractice.common.enums.Category;

public record CompletePaymentRequest(
        Long orderId,
        Long paymentId,
        Long productId,
        Long userId,
        Category category,
        int quantity
) {}
