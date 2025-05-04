package com.dio.urlshortener.infrastructure;

import com.dio.urlshortener.domain.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataShortUrlRepository extends JpaRepository<ShortUrl, String> {
}
