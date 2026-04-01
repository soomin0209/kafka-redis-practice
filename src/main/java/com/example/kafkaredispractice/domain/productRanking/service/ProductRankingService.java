package com.example.kafkaredispractice.domain.productRanking.service;

import com.example.kafkaredispractice.common.model.redis.RankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    public List<RankingDto> findProductRankingTop3InToday() {
        LocalDate currentDate = LocalDate.now();
        String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();

        Set<TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, 2);

        if (result == null || result.isEmpty()){
            return List.of();
        }

        return result.stream()
                .map(tuple -> new RankingDto(tuple.getValue(), tuple.getScore() != null ? tuple.getScore() : 0.0))
                .toList();
    }
}
