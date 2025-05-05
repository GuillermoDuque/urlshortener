package com.dio.urlshortener.presentation;

import com.dio.urlshortener.application.ShortUrlService;
import com.dio.urlshortener.presentation.dto.ShortUrlStatsResponse;
import com.dio.urlshortener.presentation.dto.ShortenUrlRequest;
import com.dio.urlshortener.presentation.dto.ShortenUrlResponse;
import com.dio.urlshortener.presentation.dto.ShortenUrlUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Short URL API", description = "Endpoints para acortar URLs, redirigir y consultar estadísticas")
@RestController
public class ShortUrlController {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);
    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @Operation(
            summary = "Crea una nueva Short URL",
            description = "Dado un `longUrl`, retorna un objeto con la Short URL generada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Short URL creada exitosamente", content = @Content(schema = @Schema(implementation = ShortenUrlResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Formato inválido de la URL", content = @Content)
            }
    )
    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shorten(
            @Valid @RequestBody ShortenUrlRequest request,
            HttpServletRequest servletRequest) {

        log.debug("shorten|in. Creating short URL for '{}'", request.longUrl());

        var response = service.createShortUrlResponse(request, servletRequest);

        log.debug("shorten|out. Short URL created: {}", response.shortUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Redirige desde un shortCode a la URL original",
            description = "Si el código es válido y está activo, redirige al cliente con un 302 (Found).",
            responses = {
                    @ApiResponse(responseCode = "302", description = "Redirección exitosa"),
                    @ApiResponse(responseCode = "404", description = "ShortCode no encontrado o inactivo", content = @Content)
            }
    )
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @Parameter(description = "Código corto a redirigir") @PathVariable String shortCode) {

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

    @Operation(
            summary = "Actualiza una Short URL existente",
            description = "Permite modificar la URL original y el estado de activación de un shortCode",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Actualización exitosa"),
                    @ApiResponse(responseCode = "404", description = "ShortCode no encontrado", content = @Content)
            }
    )
    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> update(
            @Parameter(description = "Código corto a actualizar") @PathVariable String shortCode,
            @RequestBody ShortenUrlUpdateRequest request) {

        log.debug("update|in. Updating shortCode='{}' with longUrl='{}', isActive={}",
                shortCode, request.longUrl(), request.isActive());

        return service.updateShortUrl(shortCode, request)
                .map(updated -> {
                    log.debug("update|out. shortCode='{}' updated successfully", shortCode);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("update|out. shortCode='{}' not found", shortCode);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(
            summary = "Obtiene estadísticas de una Short URL",
            description = "Devuelve estadísticas como número de accesos, fecha de creación y estado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas", content = @Content(schema = @Schema(implementation = ShortUrlStatsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "ShortCode no encontrado", content = @Content)
            }
    )
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<ShortUrlStatsResponse> getStats(
            @Parameter(description = "Código corto del que se requieren estadísticas") @PathVariable String shortCode) {

        log.debug("getStats|in. Retrieving stats for shortCode='{}'", shortCode);

        return service.getStats(shortCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("getStats|out. shortCode='{}' not found", shortCode);
                    return ResponseEntity.notFound().build();
                });
    }
}
