package com.example.kafkaredispractice.common.config.kafka.consumer;

import com.example.kafkaredispractice.common.model.kafka.event.PaymentCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    // 기본 Consumer 속성
    private Map<String, Object> baseConsumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return props;
    }

    // 기본 ConsumerFactory
    private ConsumerFactory<String, PaymentCompletedEvent> baseConsumerFactory(String groupId) {
        JacksonJsonDeserializer<PaymentCompletedEvent> deserializer = new JacksonJsonDeserializer<>(PaymentCompletedEvent.class);
        deserializer.addTrustedPackages("com.example.kafkaredispractice.common.model.kafka.event");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(groupId),
                new StringDeserializer(),
                deserializer
        );
    }


    // 인기 상품 랭킹 전용 ConsumerFactory
    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> productRankingConsumerFactory() {
        return baseConsumerFactory("product-ranking-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> productRankingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productRankingConsumerFactory());
        return factory;
    }


    // 결제 내역 기록 전용 ConsumerFactory
    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> paymentHistoryConsumerFactory() {
        return baseConsumerFactory("payment-history-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentHistoryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentHistoryConsumerFactory());
        return factory;
    }


    // 배송 내역 기록 전용 ConsumerFactory
    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> deliveryConsumerFactory() {
        return baseConsumerFactory("delivery-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> deliveryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(deliveryConsumerFactory());
        return factory;
    }
}
