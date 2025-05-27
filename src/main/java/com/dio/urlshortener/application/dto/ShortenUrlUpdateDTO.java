package com.dio.urlshortener.application.dto;

public record ShortenUrlUpdateDTO(String longUrl,
                                  Boolean isActive){}