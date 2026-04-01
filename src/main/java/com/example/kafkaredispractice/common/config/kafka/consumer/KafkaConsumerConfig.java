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

    // ConsumerFactory
    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> productRankingConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-ranking-group");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        JacksonJsonDeserializer<PaymentCompletedEvent> deserializer = new JacksonJsonDeserializer<>(PaymentCompletedEvent.class);
        deserializer.addTrustedPackages("com.example.kafkaredispractice.common.model.kafka.event");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
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
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-history-group");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        JacksonJsonDeserializer<PaymentCompletedEvent> deserializer = new JacksonJsonDeserializer<>(PaymentCompletedEvent.class);
        deserializer.addTrustedPackages("com.example.kafkaredispractice.common.model.kafka.event");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentHistoryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentHistoryConsumerFactory());
        return factory;
    }
}
