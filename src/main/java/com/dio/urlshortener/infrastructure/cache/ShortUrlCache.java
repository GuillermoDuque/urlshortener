package com.dio.urlshortener.infrastructure.cache;


import com.dio.urlshortener.config.properties.CacheProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;

import java.util.Optional;

public class ShortUrlCache {
    private Cache<String, ShortUrl> cache;
    private final CacheProperties properties;

    public ShortUrlCache(CacheProperties properties){
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        var builder = Caffeine.newBuilder();
        builder.maximumSize(properties.getMaximumSize())
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

        this.cache = builder.build();
    }

    public Optional<ShortUrl> getIfPresent(String shortCode) {
        return Optional.ofNullable(cache.getIfPresent(shortCode));
    }

    public void put(ShortUrl shortUrl) {
        cache.put(shortUrl.getShortCode(), shortUrl);
    }

    public void invalidate(String shortCode) {
        cache.invalidate(shortCode);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
