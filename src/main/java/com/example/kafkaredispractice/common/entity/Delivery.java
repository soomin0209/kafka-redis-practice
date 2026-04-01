package com.example.kafkaredispractice.common.entity;

import com.example.kafkaredispractice.common.enums.DeliveryStatus;
import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paymentId;
    private Long orderId;
    private Long productId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime statusUpdatedAt;

    private Delivery(Long paymentId, Long orderId, Long productId, Long userId, DeliveryStatus status, LocalDateTime paidAt, LocalDateTime statusUpdatedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.status = status;
        this.paidAt = paidAt;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public static Delivery from(PaymentCompletedEvent event) {
        return new Delivery(
                event.paymentId(),
                event.orderId(),
                event.productId(),
                event.userId(),
                DeliveryStatus.PREPARING,
                LocalDateTime.parse(event.paidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                LocalDateTime.parse(event.paidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)  // 결제 완료 → 배송 준비중
        );
    }

    public void markShipping(LocalDateTime now) {
        this.status = DeliveryStatus.SHIPPING;
        this.statusUpdatedAt = now;
    }

    public void markCompleted(LocalDateTime now) {
        this.status = DeliveryStatus.COMPLETED;
        this.statusUpdatedAt = now;
    }
}