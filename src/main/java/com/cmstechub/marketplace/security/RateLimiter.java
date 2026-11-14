package com.cmstechub.marketplace.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 15 * 60;

    private static class Attempts {
        int count;
        Instant windowStart;
    }

    private final Map<String, Attempts> attemptsByKey = new HashMap<>();

    public boolean isAllowed(String key) {
        cleanup();
        Attempts a = attemptsByKey.get(key);
        Instant now = Instant.now();

        if (a == null || a.windowStart.plusSeconds(WINDOW_SECONDS).isBefore(now)) {
            Attempts fresh = new Attempts();
            fresh.count = 1;
            fresh.windowStart = now;
            attemptsByKey.put(key, fresh);
            return true;
        }
        if (a.count >= MAX_ATTEMPTS) return false;
        a.count++;
        return true;
    }

    public long secondsUntilReset(String key) {
        Attempts a = attemptsByKey.get(key);
        if (a == null) return 0;
        long remaining = a.windowStart.plusSeconds(WINDOW_SECONDS).getEpochSecond()
                - Instant.now().getEpochSecond();
        return Math.max(0, remaining);
    }

    public void reset(String key) {
        attemptsByKey.remove(key);
    }

    private void cleanup() {
        Instant now = Instant.now();
        attemptsByKey.entrySet().removeIf(
                e -> e.getValue().windowStart.plusSeconds(WINDOW_SECONDS).isBefore(now)
        );
    }
}
