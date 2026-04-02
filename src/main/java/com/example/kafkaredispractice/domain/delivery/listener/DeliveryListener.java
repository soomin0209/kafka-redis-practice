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

        // 테스트용 : 특정 productId에 대해서는 일부러 예외 발생
        if (event.productId() == 999L) {
            log.info("[Delivery-Consumer] 테스트용 예외 발생 - productId=999");
            throw new RuntimeException("테스트용 배송 에러");
        }

        deliveryService.createDeliveryFromPayment(event);
    }
}
