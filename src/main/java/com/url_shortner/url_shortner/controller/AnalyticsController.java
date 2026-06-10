package com.url_shortner.url_shortner.controller;

import com.url_shortner.url_shortner.dto.AnalyticsResponse;
import com.url_shortner.url_shortner.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping(value = "analytics/{shortcode}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortcode){
        return ResponseEntity.status(HttpStatus.OK)
                .body(analyticsService.getAnalytics(shortcode));
    }

}
