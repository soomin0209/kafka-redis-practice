package com.example.kafkaredispractice.domain.paymentHistory.service;

import com.example.kafkaredispractice.common.entity.PaymentHistory;
import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.paymentHistory.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public void savePaymentHistory(PaymentCompletedEvent event) {
        // 이미 처리된 paymentId는 재처리 X
        if (paymentHistoryRepository.existsByPaymentId(event.paymentId())) {
            log.info("[DB] 이미 처리된 결제 - 재처리 스킵 : paymentId={}", event.paymentId());
            return;
        }

        PaymentHistory paymentHistory = PaymentHistory.from(event);
        paymentHistoryRepository.save(paymentHistory);

        log.info("[DB] 결제 내역 저장 완료 : paymentId={}, productId={}", event.paymentId(), event.productId());
    }
}
