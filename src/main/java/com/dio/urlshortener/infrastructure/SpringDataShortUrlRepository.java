package com.dio.urlshortener.infrastructure;

import com.dio.urlshortener.domain.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataShortUrlRepository extends JpaRepository<ShortUrl, String> {
    Optional<ShortUrl> findByLongUrl(String longUrl);
}
