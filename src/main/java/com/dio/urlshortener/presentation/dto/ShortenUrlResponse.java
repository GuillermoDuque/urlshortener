package com.dio.urlshortener.presentation.dto;

import java.time.Instant;

public record ShortenUrlResponse(String shortCode,
                                 String shortUrl,
                                 String longUrl,
                                 boolean isActive,
                                 Instant createdAt) {

}
