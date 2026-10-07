package com.github.inzmamkhan.ziplink_backend.service.impl;

import com.github.inzmamkhan.ziplink_backend.dto.request.CreateShortUrlRequest;
import com.github.inzmamkhan.ziplink_backend.dto.response.ShortUrlResponse;
import com.github.inzmamkhan.ziplink_backend.dto.response.UrlAnalyticsResponse;
import com.github.inzmamkhan.ziplink_backend.entity.UrlEntity;
import com.github.inzmamkhan.ziplink_backend.exception.InvalidUrlException;
import com.github.inzmamkhan.ziplink_backend.exception.UrlNotFoundException;
import com.github.inzmamkhan.ziplink_backend.repository.UrlRepository;
import com.github.inzmamkhan.ziplink_backend.service.Base62Service;
import com.github.inzmamkhan.ziplink_backend.service.UrlShortenerService;
import com.github.inzmamkhan.ziplink_backend.util.UrlValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private static final String CACHE_PREFIX_URL = "url:";
    private static final String CACHE_PREFIX_CLICK = "clicks:";

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private Base62Service base62Service;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request, String baseUrl) {
        if (request == null || request.getOriginalUrl() == null) {
            throw new InvalidUrlException("Original URL must not be null");
        }

        String sanitizedUrl = UrlValidatorUtil.sanitizeUrl(request.getOriginalUrl());
        if (!UrlValidatorUtil.isValidUrl(sanitizedUrl)) {
            throw new InvalidUrlException("Invalid URL format: " + request.getOriginalUrl());
        }

        UrlEntity entity = UrlEntity.builder()
                .originalUrl(sanitizedUrl)
                .shortKey("temp")
                .clickCount(0L)
                .build();

        UrlEntity savedEntity = urlRepository.save(entity);

        String shortKey = base62Service.encode(savedEntity.getId());
        savedEntity.setShortKey(shortKey);
        urlRepository.save(savedEntity);

        // Cache in Redis (1 day TTL)
        redisTemplate.opsForValue().set(CACHE_PREFIX_URL + shortKey, sanitizedUrl, 24, TimeUnit.HOURS);

        String formattedBaseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        String fullShortUrl = formattedBaseUrl + shortKey;

        return new ShortUrlResponse(
                shortKey,
                fullShortUrl,
                sanitizedUrl,
                savedEntity.getCreatedAt()
        );
    }

    @Override
    public String getOriginalUrl(String shortKey) {
        // 1. Check Redis Cache
        String cachedUrl = (String) redisTemplate.opsForValue().get(CACHE_PREFIX_URL + shortKey);
        if (cachedUrl != null) {
            redisTemplate.opsForValue().increment(CACHE_PREFIX_CLICK + shortKey);
            return cachedUrl;
        }

        // 2. Cache miss -> Database lookup
        UrlEntity entity = urlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new UrlNotFoundException("Short URL key not found: " + shortKey));

        redisTemplate.opsForValue().set(CACHE_PREFIX_URL + shortKey, entity.getOriginalUrl(), 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().increment(CACHE_PREFIX_CLICK + shortKey);

        return entity.getOriginalUrl();
    }

    @Override
    public UrlAnalyticsResponse getUrlAnalytics(String shortKey) {
        UrlEntity entity = urlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new UrlNotFoundException("Short URL key not found: " + shortKey));

        String pendingClicksStr = (String) redisTemplate.opsForValue().get(CACHE_PREFIX_CLICK + shortKey);
        long pendingClicks = 0L;
        if (pendingClicksStr != null) {
            try {
                pendingClicks = Long.parseLong(pendingClicksStr);
            } catch (NumberFormatException ignored) {}
        }

        long totalClicks = entity.getClickCount() + pendingClicks;

        return new UrlAnalyticsResponse(
                entity.getShortKey(),
                entity.getOriginalUrl(),
                totalClicks,
                entity.getCreatedAt(),
                entity.getLastAccessedAt()
        );
    }
}