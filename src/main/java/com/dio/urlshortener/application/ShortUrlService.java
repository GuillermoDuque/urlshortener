package com.dio.urlshortener.application;

import com.dio.urlshortener.config.properties.AppProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.dio.urlshortener.domain.repository.ShortUrlRepository;
import com.dio.urlshortener.domain.service.ShortCodeGenerator;
import com.dio.urlshortener.infrastructure.cache.ShortUrlCache;
import com.dio.urlshortener.presentation.dto.ShortenUrlRequest;
import com.dio.urlshortener.presentation.dto.ShortenUrlResponse;
import com.dio.urlshortener.presentation.dto.ShortenUrlUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ShortUrlService {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlService.class);

    private final ShortUrlRepository repository;
    private final ShortUrlCache cache;
    private final AppProperties properties;
    private final ShortCodeGenerator generator;

    public ShortUrlService(ShortUrlRepository repository, ShortUrlCache cache, AppProperties properties, ShortCodeGenerator generator) {
        this.repository = repository;
        this.cache = cache;
        this.properties = properties;
        this.generator = generator;
    }

    public ShortenUrlResponse createShortUrlResponse(ShortenUrlRequest request, HttpServletRequest servletRequest) {
        log.debug("createShortUrlResponse|in. Building full response for longUrl='{}'", request.longUrl());

        ShortUrl shortUrl = createShortUrl(request);

        String base = Optional.ofNullable(properties.getBaseUrl())
                .orElseGet(() -> servletRequest.getRequestURL()
                        .toString()
                        .replace(servletRequest.getRequestURI(), ""));

        String fullShortUrl = base + "/" + shortUrl.getShortCode();

        log.debug("createShortUrlResponse|out. Returning shortUrl='{}'", fullShortUrl);

        return new ShortenUrlResponse(
                shortUrl.getShortCode(),
                fullShortUrl,
                shortUrl.getLongUrl(),
                shortUrl.isActive(),
                shortUrl.getCreatedAt()
        );
    }



    public ShortUrl createShortUrl(ShortenUrlRequest request) {
        log.debug("createShortUrl|in. Generating or retrieving short URL for longUrl='{}'", request.longUrl());

        Optional<ShortUrl> existing = cache.getByLongUrl(request.longUrl());
        if (existing.isPresent()) {
            log.debug("createShortUrl|cache hit. Reusing shortCode='{}'", existing.get().getShortCode());
            return existing.get();
        }

        Optional<ShortUrl> inDb = repository.findByLongUrl(request.longUrl());
        if (inDb.isPresent()) {
            cache.put(inDb.get());
            log.debug("createShortUrl|db hit. Reusing shortCode='{}'", inDb.get().getShortCode());
            return inDb.get();
        }

        var shortUrl = new ShortUrl(generator.generate(), request.longUrl());
        repository.save(shortUrl);
        cache.put(shortUrl);

        log.debug("createShortUrl|out. Created new shortCode='{}'", shortUrl.getShortCode());
        return shortUrl;
    }


    public Optional<ShortUrl> updateShortUrl(String shortCode, ShortenUrlUpdateRequest request) {
        log.debug("updateShortUrl|in. Attempting to update shortCode='{}'", shortCode);

        return repository.findByShortCode(shortCode).map(existing -> {
            if (request.longUrl() != null) {
                existing.setLongUrl(request.longUrl());
            }
            if (request.isActive() != null) {
                existing.setActive(request.isActive());
            }

            repository.save(existing);
            cache.put(existing);

            log.debug("updateShortUrl|out. Updated shortCode='{}' successfully. longUrl='{}', isActive={}",
                    existing.getShortCode(), existing.getLongUrl(), existing.isActive());

            return existing;
        });
    }


    public Optional<ShortUrl> findByShortCode(String shortCode) {
        log.debug("findByShortCode|in. Attempting to resolve shortCode='{}' from cache", shortCode);
        return cache.getIfPresent(shortCode)
                .or(() -> findInDbAndCacheIfPresent(shortCode));
    }

    public Optional<ShortUrl> resolveShortUrl(String shortCode) {
        log.debug("resolveShortUrl|in. Looking for shortCode='{}'", shortCode);

        return findByShortCode(shortCode)
                .filter(ShortUrl::isActive)
                .map(shortUrl -> {
                    log.debug("resolveShortUrl|out. Found and active. longUrl='{}'", shortUrl.getLongUrl());
                    return shortUrl;
                });
    }

    private Optional<ShortUrl> findInDbAndCacheIfPresent(String shortCode) {
        log.debug("findInDbAndCacheIfPresent|in. shortCode='{}' not found in cache. Querying DB...", shortCode);
        return repository.findByShortCode(shortCode)
                .map(found -> {
                    cache.put(found);
                    log.debug("findInDbAndCacheIfPresent|out. shortCode='{}' found in DB and cached", shortCode);
                    return found;
                });
    }

}
