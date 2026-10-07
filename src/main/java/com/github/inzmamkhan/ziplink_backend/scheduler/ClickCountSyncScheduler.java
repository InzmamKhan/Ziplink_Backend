package com.github.inzmamkhan.ziplink_backend.scheduler;

import com.github.inzmamkhan.ziplink_backend.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;

@Component
public class ClickCountSyncScheduler {

    private static final String CACHE_PREFIX_CLICK = "clicks:*";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Scheduled(fixedRate = 60000) // Sync every 60 seconds
    @Transactional
    public void syncClickCountsToDatabase() {
        Set<String> clickKeys = redisTemplate.keys(CACHE_PREFIX_CLICK);

        if (clickKeys == null || clickKeys.isEmpty()) {
            return;
        }

        for (String key : clickKeys) {
            String shortKey = key.replace("clicks:", "");
            Object rawCount = redisTemplate.opsForValue().get(key);

            if (rawCount != null) {
                try {
                    long pendingClicks = Long.parseLong(rawCount.toString());
                    if (pendingClicks > 0) {
                        urlRepository.incrementClickCountAndAccessTime(shortKey, pendingClicks, ZonedDateTime.now());
                        redisTemplate.delete(key);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }
}