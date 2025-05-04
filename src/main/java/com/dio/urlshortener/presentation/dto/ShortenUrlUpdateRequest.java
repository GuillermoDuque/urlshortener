package com.dio.urlshortener.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShortenUrlUpdateRequest(
        String longUrl,
        Boolean isActive
) {}
