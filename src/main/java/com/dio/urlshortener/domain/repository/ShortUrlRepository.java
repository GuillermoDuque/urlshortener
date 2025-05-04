package com.dio.urlshortener.domain.repository;

import com.dio.urlshortener.domain.model.ShortUrl;

import java.util.Optional;

public interface ShortUrlRepository {
    ShortUrl save(ShortUrl url);
    Optional<ShortUrl> findByShortCode(String shortCode);
}
