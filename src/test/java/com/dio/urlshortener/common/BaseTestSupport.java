package com.dio.urlshortener.common;

import com.dio.urlshortener.config.properties.CacheProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.dio.urlshortener.infrastructure.cache.ShortUrlCache;
import com.dio.urlshortener.infrastructure.generator.Base62ShortCodeGenerator;

import java.time.Duration;

public abstract class BaseTestSupport {

    protected static final String SHORT_CODE = "abc123";
    protected static final String LONG_URL = "https://example.com";
    protected static final String NEW_URL = "https://new.com";
    protected static final String DISABLED_URL = "https://inactive.com";
    protected static final String DISABLED_CODE = "disabled123";

    protected ShortUrl createUrl(String code, String longUrl) {
        return new ShortUrl(code, longUrl);
    }
}
