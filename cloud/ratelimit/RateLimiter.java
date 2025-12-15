package com.zanta.lfp.cloud.ratelimit;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private Map<String, RateLimitInfo> clientLimits = new ConcurrentHashMap<>();

    private static class RateLimitInfo {
        int requestCount;
        long windowStart;
        int maxRequests;
        long windowSizeMs;

        RateLimitInfo(int maxRequests, long windowSizeMs) {
            this.maxRequests = maxRequests;
            this.windowSizeMs = windowSizeMs;
            this.windowStart = System.currentTimeMillis();
            this.requestCount = 0;
        }
    }

    public boolean isAllowed(String clientId, int maxRequests, long windowSizeMs) {
        RateLimitInfo info = clientLimits.computeIfAbsent(clientId, 
            k -> new RateLimitInfo(maxRequests, windowSizeMs));

        long now = System.currentTimeMillis();
        
        if (now - info.windowStart >= info.windowSizeMs) {
            info.windowStart = now;
            info.requestCount = 0;
        }

        if (info.requestCount >= info.maxRequests) {
            System.out.println("Rate Limiter: Request denied for " + clientId + 
                             " (limit: " + maxRequests + "/" + windowSizeMs + "ms)");
            return false;
        }

        info.requestCount++;
        System.out.println("Rate Limiter: Request allowed for " + clientId + 
                         " (" + info.requestCount + "/" + maxRequests + ")");
        return true;
    }

    public int getRemainingRequests(String clientId) {
        RateLimitInfo info = clientLimits.get(clientId);
        if (info == null) {
            return 0;
        }
        return Math.max(0, info.maxRequests - info.requestCount);
    }

    public void reset(String clientId) {
        clientLimits.remove(clientId);
        System.out.println("Rate Limiter: Reset rate limit for " + clientId);
    }
}
