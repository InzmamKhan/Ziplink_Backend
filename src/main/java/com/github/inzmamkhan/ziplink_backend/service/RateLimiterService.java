package com.github.inzmamkhan.ziplink_backend.service;

public interface RateLimiterService {

    /**
     * Checks if a request from the given client IP address is allowed within the current rate limit window.
     *
     * @param clientIp The IP address of the incoming caller
     * @return true if allowed, false if limit exceeded
     */
    boolean isAllowed(String clientIp);
}