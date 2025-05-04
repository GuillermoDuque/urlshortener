package com.dio.urlshortener.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShortenUrlRequest(
        @Pattern(
                regexp = "https?://.+",
                message = "Debe ser una URL válida que comience con http:// o https://"
        )
        @NotBlank
        String longUrl
) {
}
