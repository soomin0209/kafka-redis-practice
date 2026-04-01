package com.example.kafkaredispractice.common.entity;

import com.example.kafkaredispractice.common.enums.Category;
import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@Table(name = "payment_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paymentId;
    private Long orderId;
    private Long productId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int quantity;
    private LocalDateTime paidAt;

    private PaymentHistory(Long paymentId, Long orderId, Long productId, Long userId, Category category, int quantity, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.category = category;
        this.quantity = quantity;
        this.paidAt = paidAt;
    }

    public static PaymentHistory from(PaymentCompletedEvent event) {
        return new PaymentHistory(
                event.paymentId(),
                event.orderId(),
                event.productId(),
                event.userId(),
                event.category(),
                event.quantity(),
                LocalDateTime.parse(event.paidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}