package com.example.kafkaredispractice.domain.payment.producer;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.kafkaredispractice.common.model.kafka.topic.KafkaTopic.TOPIC_PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

    public void send(PaymentCompletedEvent event) {
        paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event);
    }
}
