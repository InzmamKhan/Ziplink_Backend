package com.github.inzmamkhan.ziplink_backend.service;

import com.github.inzmamkhan.ziplink_backend.dto.request.CreateShortUrlRequest;
import com.github.inzmamkhan.ziplink_backend.dto.response.ShortUrlResponse;
import com.github.inzmamkhan.ziplink_backend.dto.response.UrlAnalyticsResponse;

public interface UrlShortenerService {

    /**
     * Shortens a long URL and saves it to PostgreSQL & Redis.
     */
    ShortUrlResponse createShortUrl(CreateShortUrlRequest request, String baseUrl);

    /**
     * Retrieves the original long URL associated with a short key.
     */
    String getOriginalUrl(String shortKey);

    /**
     * Fetches analytics details for a given short key.
     */
    UrlAnalyticsResponse getUrlAnalytics(String shortKey);
}