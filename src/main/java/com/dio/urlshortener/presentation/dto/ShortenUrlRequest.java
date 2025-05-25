package com.dio.urlshortener.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShortenUrlRequest(
        @Pattern(
                regexp = "https?://.+",
                message = "Debe ser una URL válida que comience con http:// o https://"
        )
        @NotBlank
        String longUrl,
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]{3,30}$",
                message = "El shortCode sugerido solo puede contener letras, números, guiones o guiones bajos (3-30 caracteres)"
        )
        String customShortCode
) {
}
