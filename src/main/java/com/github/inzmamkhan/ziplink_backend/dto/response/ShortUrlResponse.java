package com.github.inzmamkhan.ziplink_backend.dto.response;

import java.time.ZonedDateTime;

public class ShortUrlResponse {

    private String shortKey;
    private String shortUrl;
    private String originalUrl;
    private ZonedDateTime createdAt;

    public ShortUrlResponse() {
    }

    public ShortUrlResponse(String shortKey, String shortUrl, String originalUrl, ZonedDateTime createdAt) {
        this.shortKey = shortKey;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}