package com.example.kafkaredispractice.domain.productRanking.listener;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import com.example.kafkaredispractice.domain.productRanking.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.kafkaredispractice.common.model.kafka.topic.KafkaTopic.TOPIC_PAYMENT_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRankingListener {

    private final ProductRankingService productRankingService;

    @KafkaListener(
            topics = TOPIC_PAYMENT_COMPLETED,
            groupId = "product-ranking-group",
            containerFactory = "productRankingKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {
        LocalDateTime paidAt = LocalDateTime.parse(event.paidAt());
        LocalDate currentDate = paidAt.toLocalDate();
        productRankingService.increaseProductRanking(event.productId(),currentDate);
    }
}
