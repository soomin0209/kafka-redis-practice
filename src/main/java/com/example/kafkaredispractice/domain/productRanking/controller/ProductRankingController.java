package com.example.kafkaredispractice.domain.productRanking.controller;

import com.example.kafkaredispractice.common.model.redis.RankingDto;
import com.example.kafkaredispractice.domain.productRanking.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking/product")
public class ProductRankingController {

    private final ProductRankingService productRankingService;

    @GetMapping("/today")
    public ResponseEntity<List<RankingDto>> findProductRankingTop3InToday() {
        return ResponseEntity.status(HttpStatus.OK).body(productRankingService.findProductRankingTop3InToday());
    }
}
