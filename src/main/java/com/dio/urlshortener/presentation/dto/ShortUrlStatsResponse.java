package com.dio.urlshortener.presentation.dto;

import java.time.Instant;

public record ShortUrlStatsResponse(
        String shortCode,
        String longUrl,
        boolean isActive,
        long accessCount,
        Instant createdAt
) {}
