package com.example.kafkaredispractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaRedisPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaRedisPracticeApplication.class, args);
    }

}
