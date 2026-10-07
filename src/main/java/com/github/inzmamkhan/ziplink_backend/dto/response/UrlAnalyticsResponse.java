package com.github.inzmamkhan.ziplink_backend.dto.response;

import java.time.ZonedDateTime;

public class UrlAnalyticsResponse {

    private String shortKey;
    private String originalUrl;
    private Long clickCount;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastAccessedAt;

    public UrlAnalyticsResponse() {
    }

    public UrlAnalyticsResponse(String shortKey, String originalUrl, Long clickCount, ZonedDateTime createdAt, ZonedDateTime lastAccessedAt) {
        this.shortKey = shortKey;
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(ZonedDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}