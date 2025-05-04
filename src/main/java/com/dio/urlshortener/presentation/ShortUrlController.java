package com.dio.urlshortener.presentation;

import com.dio.urlshortener.application.ShortUrlService;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.dio.urlshortener.presentation.dto.ShortenUrlRequest;
import com.dio.urlshortener.presentation.dto.ShortenUrlResponse;
import com.dio.urlshortener.presentation.dto.ShortenUrlUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShortUrlController {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shorten(
            @Valid @RequestBody ShortenUrlRequest request,
            HttpServletRequest servletRequest) {

        log.info("shorten|in. Creating short URL for '{}'", request.longUrl());

        var response = service.createShortUrlResponse(request, servletRequest);

        log.info("shorten|out. Short URL created: {}", response.shortUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        log.debug("redirect|in. shortCode='{}'", shortCode);

        return service.resolveShortUrl(shortCode)
                .map(shortUrl -> {
                    log.debug("redirect|out. Redirecting to '{}'", shortUrl.getLongUrl());
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .header("Location", shortUrl.getLongUrl())
                            .<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("redirect|out. shortCode='{}' not found or inactive", shortCode);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> update(
            @PathVariable String shortCode,
            @RequestBody ShortenUrlUpdateRequest request) {

        log.info("update|in. Updating shortCode='{}' with longUrl='{}', isActive={}",
                shortCode, request.longUrl(), request.isActive());

        return service.updateShortUrl(shortCode, request)
                .map(updated -> {
                    log.info("update|out. shortCode='{}' updated successfully", shortCode);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("update|out. shortCode='{}' not found", shortCode);
                    return ResponseEntity.notFound().build();
                });
    }



}
