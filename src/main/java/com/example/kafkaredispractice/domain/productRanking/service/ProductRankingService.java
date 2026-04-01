package com.example.kafkaredispractice.domain.productRanking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String PRODUCT_RANKING_DAILY_KEY = "product:ranking:";

    public void increaseProductRanking(Long productId, LocalDate currentDate) {
        String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();
        stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(productId), 1);
    }
}
