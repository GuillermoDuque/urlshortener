package com.dio.urlshortener.infrastructure;

import com.dio.urlshortener.domain.model.ShortUrl;
import com.dio.urlshortener.domain.repository.ShortUrlRepository;

import java.util.Optional;

public class JpaShortUrlRepository implements ShortUrlRepository {

    private final SpringDataShortUrlRepository delegate;

    public JpaShortUrlRepository(SpringDataShortUrlRepository delegate){
        this.delegate = delegate;
    }

    @Override
    public ShortUrl save(ShortUrl url) {
        return delegate.save(url);
    }

    @Override
    public Optional<ShortUrl> findByShortCode(String shortCode) {
        return delegate.findById(shortCode);
    }

    @Override
    public Optional<ShortUrl> findByLongUrl(String longUrl) {
        return delegate.findByLongUrl(longUrl);
    }
}
