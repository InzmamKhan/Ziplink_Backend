package com.github.inzmamkhan.ziplink_backend.controller;

import com.github.inzmamkhan.ziplink_backend.dto.response.UrlAnalyticsResponse;
import com.github.inzmamkhan.ziplink_backend.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/{shortKey}")
    public ResponseEntity<UrlAnalyticsResponse> getUrlAnalytics(@PathVariable String shortKey) {
        UrlAnalyticsResponse analytics = urlShortenerService.getUrlAnalytics(shortKey);
        return ResponseEntity.ok(analytics);
    }
}