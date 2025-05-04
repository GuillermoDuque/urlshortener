package com.dio.urlshortener.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.cache.short-url")
public class CacheProperties {
    private Duration expireAfterWrite = Duration.ofMinutes(10);
    private Duration expireAfterAccess;
    private long maximumSize = 10_000;
    private int initialCapacity = 100;
    private boolean recordStats = false;

    public Duration getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(Duration expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public Duration getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public void setExpireAfterAccess(Duration expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public boolean isRecordStats() {
        return recordStats;
    }

    public void setRecordStats(boolean recordStats) {
        this.recordStats = recordStats;
    }
}