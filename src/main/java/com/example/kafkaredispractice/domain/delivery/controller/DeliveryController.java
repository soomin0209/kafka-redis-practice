package com.example.kafkaredispractice.domain.delivery.controller;

import com.example.kafkaredispractice.domain.delivery.model.response.DeliveryResponse;
import com.example.kafkaredispractice.domain.delivery.service.DeliveryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryCacheService deliveryCacheService;

    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> findUserDeliveries(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(deliveryCacheService.findUserDeliveries(userId));
    }
}
