package com.dio.urlshortener.infrastructure.cache;

import com.dio.urlshortener.config.properties.CacheProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;

import java.util.Optional;

public class ShortUrlCache {

    private Cache<String, ShortUrl> shortCodeCache;
    private Cache<String, ShortUrl> longUrlCache;
    private final CacheProperties properties;

    public ShortUrlCache(CacheProperties properties){
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        var builder = Caffeine.newBuilder()
                .maximumSize(properties.getMaximumSize())
                .initialCapacity(properties.getInitialCapacity());

        if (properties.getExpireAfterWrite() != null) {
            builder.expireAfterWrite(properties.getExpireAfterWrite());
        }
        if (properties.getExpireAfterAccess() != null) {
            builder.expireAfterAccess(properties.getExpireAfterAccess());
        }
        if (properties.isRecordStats()) {
            builder.recordStats();
        }

        this.shortCodeCache = builder.build();
        this.longUrlCache = builder.build();
    }

    public Optional<ShortUrl> getIfPresent(String shortCode) {
        return Optional.ofNullable(shortCodeCache.getIfPresent(shortCode));
    }

    public Optional<ShortUrl> getByLongUrl(String longUrl) {
        return Optional.ofNullable(longUrlCache.getIfPresent(longUrl));
    }

    public void put(ShortUrl shortUrl) {
        shortCodeCache.put(shortUrl.getShortCode(), shortUrl);
        longUrlCache.put(shortUrl.getLongUrl(), shortUrl);
    }

    public void invalidate(String shortCode, String longUrl) {
        shortCodeCache.invalidate(shortCode);
        longUrlCache.invalidate(longUrl);
    }

    public void clear() {
        shortCodeCache.invalidateAll();
        longUrlCache.invalidateAll();
    }
}
