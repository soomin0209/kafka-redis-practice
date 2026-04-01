package com.example.kafkaredispractice.domain.paymentHistory.listener;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.paymentHistory.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafkaredispractice.common.model.kafka.topic.KafkaTopic.TOPIC_PAYMENT_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHistoryListener {

    private final PaymentHistoryService paymentHistoryService;

    @KafkaListener(
            topics = TOPIC_PAYMENT_COMPLETED,
            groupId = "payment-history-group",
            containerFactory = "paymentHistoryKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {
        log.info("[Consumer-History] 결제 완료 이벤트 수신 : paymentId={}, productId={}", event.paymentId(), event.productId());

        paymentHistoryService.savePaymentHistory(event);
    }
}
