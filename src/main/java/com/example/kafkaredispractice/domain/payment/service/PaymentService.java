package com.example.kafkaredispractice.domain.payment.service;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.payment.model.request.CompletePaymentRequest;
import com.example.kafkaredispractice.domain.payment.producer.PaymentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProducer producer;

    public void paymentComplete(CompletePaymentRequest request) {

        // 실제 결제 로직 제외하고 성공했다고 가정

        // 직렬화 안정성을 위해 LocalDateTime을 ISO 형식의 문자열로 변환
        String paidAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                request.paymentId(),
                request.orderId(),
                request.productId(),
                request.userId(),
                request.category(),
                request.quantity(),
                paidAt
        );

        producer.send(event);
    }
}
