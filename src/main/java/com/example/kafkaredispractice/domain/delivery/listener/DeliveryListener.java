package com.example.kafkaredispractice.domain.delivery.listener;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafkaredispractice.common.model.kafka.topic.KafkaTopic.TOPIC_PAYMENT_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final DeliveryService deliveryService;

    @KafkaListener(
            topics = TOPIC_PAYMENT_COMPLETED,
            groupId = "delivery-group",
            containerFactory = "deliveryKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {
        log.info("[Delivery-Consumer] 결제 완료 이벤트 수신 : orderId={}, userId={}", event.orderId(), event.userId());

        deliveryService.createDeliveryFromPayment(event);
    }
}
